def call(Map params) {
    stage('Checkout') {
    checkout([$class: 'GitSCM', 
        branches: [[name: "${params.BRANCH}"]], 
        userRemoteConfigs: [[url: "${params.GITHUB_REPO}", credentialsId: "${params.GITHUB_CREDENTIALS_ID}"]]
        ])
    }
}
