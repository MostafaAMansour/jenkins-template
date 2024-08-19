def call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('Clone Repository') {
                steps {
                    checkout scm: [$class: 'GitSCM', branches: [[name: pipelineParams.BRANCH]], userRemoteConfigs: [[url: pipelineParams.GITHUB_REPO, credentialsId: pipelineParams.GITHUB_CREDENTIALS_ID]]]
                }
            }

            stage('Build and Push Docker Image') {
                steps {
                    withCredentials([usernamePassword(credentialsId: pipelineParams.DOCKER_CREDENTIALS_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh """
                        echo "${PASSWORD}" | docker login -u "${USERNAME}" --password-stdin
                        docker build -t ${pipelineParams.DOCKER_IMAGE} .
                        docker push ${pipelineParams.DOCKER_IMAGE}
                        docker logout
                        """
                    }
                }
            }
        }

        post {
            success {
                mail to: pipelineParams.RECIPIENT_EMAIL, subject: "Build SUCCESS", body: "The build was successful."
            }
            failure {
                mail to: pipelineParams.RECIPIENT_EMAIL, subject: "Build FAILURE", body: "The build failed."
            }
        }
    }
}
