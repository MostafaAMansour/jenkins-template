def call(String GITHUB_REPO, String GITHUB_CREDENTIALS_ID, String BRANCH, String DOCKER_IMAGE, String DOCKER_CREDENTIALS_ID, String RECIPIENT_EMAIL) {
    pipeline {
        agent any

        parameters {
            string(name: 'GITHUB_REPO', defaultValue: GITHUB_REPO, description: 'GitHub repository URL')
            string(name: 'GITHUB_CREDENTIALS_ID', defaultValue: GITHUB_CREDENTIALS_ID, description: 'GitHub credentials ID')
            string(name: 'BRANCH', defaultValue: BRANCH, description: 'Branch to clone')
            string(name: 'DOCKER_IMAGE', defaultValue: DOCKER_IMAGE, description: 'Docker image name')
            string(name: 'DOCKER_CREDENTIALS_ID', defaultValue: DOCKER_CREDENTIALS_ID, description: 'DockerHub credentials ID')
            string(name: 'RECIPIENT_EMAIL', defaultValue: RECIPIENT_EMAIL, description: 'Recipient email address')
        }

        stages {
            stage('Clone Repository') {
                steps {
                    script {
                        checkout([$class: 'GitSCM',
                                  branches: [[name: "${params.BRANCH}"]],
                                  userRemoteConfigs: [[url: "${params.GITHUB_REPO}", credentialsId: "${params.GITHUB_CREDENTIALS_ID}"]]
                        ])
                    }
                }
            }

            stage('Push Docker Images') {
                steps {
                    script {
                        // Push the built images to Docker Hub
                        withCredentials([usernamePassword(credentialsId: "${params.DOCKER_CREDENTIALS_ID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            sh """
                            echo "${PASSWORD}" | docker login -u "${USERNAME}" --password-stdin
                            docker compose push "${params.DOCKER_IMAGE}"
                            docker logout
                            """
                        }
                    }
                }
            }
        }

        post {
            success {
                mail subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: "Good news! Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' succeeded.",
                        to: "${params.RECIPIENT_EMAIL}"
            }
            failure {
                mail subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' failed. Please check the logs for details.",
                        to: "${params.RECIPIENT_EMAIL}"
            }
            cleanup {
                cleanWs()
            }
        }
    }
}
