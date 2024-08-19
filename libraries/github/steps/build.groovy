def call(Map pipelineParams) {
    pipeline {
        agent any

        parameters {
            string(name: 'GITHUB_REPO', defaultValue: '', description: 'GitHub repository URL')
            string(name: 'GITHUB_CREDENTIALS_ID', defaultValue: '', description: 'GitHub credentials ID')
            string(name: 'BRANCH', defaultValue: 'main', description: 'Branch to clone')
            string(name: 'DOCKER_IMAGE', defaultValue: '', description: 'Docker image name')
            string(name: 'DOCKER_CREDENTIALS_ID', defaultValue: '', description: 'DockerHub credentials ID')
            string(name: 'RECIPIENT_EMAIL', defaultValue: '', description: 'Recipient email address')
        }

        stages {
            stage('Clone Repository') {
                steps {
                    script {
                        checkout([$class: 'GitSCM',
                                  branches: [[name: "${pipelineParams.BRANCH}"]],
                                  userRemoteConfigs: [[url: "${pipelineParams.GITHUB_REPO}", credentialsId: "${pipelineParams.GITHUB_CREDENTIALS_ID}"]]
                        ])
                    }
                }
            }

            stage('Push Docker Images') {
                steps {
                    script {
                        // Push the built images to Docker Hub
                        withCredentials([usernamePassword(credentialsId: "${pipelineParams.DOCKER_CREDENTIALS_ID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            sh """
                            echo "${PASSWORD}" | docker login -u "${USERNAME}" --password-stdin
                            docker compose push "${pipelineParams.DOCKER_IMAGE}"
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
                        to: "${pipelineParams.RECIPIENT_EMAIL}"
            }
            failure {
                mail subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' failed. Please check the logs for details.",
                        to: "${pipelineParams.RECIPIENT_EMAIL}"
            }
            cleanup {
                cleanWs()
            }
        }
    }
}
