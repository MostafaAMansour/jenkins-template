def call(Map params) {
    node() {
        stage('Checkout') {
            checkout([$class: 'GitSCM', 
                branches: [[name: "${params.BRANCH}"]], 
                userRemoteConfigs: [[url: "${params.GITHUB_REPO}", credentialsId: "${params.GITHUB_CREDENTIALS_ID}"]]
            ])
        }
        stage('Docker push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${params.DOCKER_CREDENTIALS_ID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh '''
                    echo "\${PASSWORD}" | docker login -u "\${USERNAME}" --password-stdin
                    docker push "\${params.DOCKER_IMAGE}"
                    docker logout
                    '''
                }
            }
        }
    }
}
