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
        echo "hello world"
        // stages {
        //     stage('Clone Repository') {
        //         steps {
        //             script {
        //                 git branch: "${BRANCH}", credentialsId: "${GITHUB_CREDENTIALS_ID}", url: "${GITHUB_REPO}"

        //             }
        //         }
        //     }
        // }
    }
}
