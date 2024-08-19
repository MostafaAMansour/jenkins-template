def call(String GITHUB_REPO, String GITHUB_CREDENTIALS_ID, String BRANCH, String DOCKER_IMAGE, String DOCKER_CREDENTIALS_ID, String RECIPIENT_EMAIL) {
    // git branch: "${BRANCH}", credentialsId: "${GITHUB_CREDENTIALS_ID}", url: "${GITHUB_REPO}"
    echo "hello world"
    // withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
    //     sh """
    //     echo "${PASSWORD}" | docker login -u "${USERNAME}" --password-stdin
    //     docker compose push "${DOCKER_IMAGE}"
    //     docker logout
    //     """
    }

        // post {
        //     success {
        //         mail subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        //                 body: "Good news! Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' succeeded.",
        //                 to: "${params.RECIPIENT_EMAIL}"
        //     }
        //     failure {
        //         mail subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        //                 body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' failed. Please check the logs for details.",
        //                 to: "${params.RECIPIENT_EMAIL}"
        //     }
        //     cleanup {
        //         cleanWs()
        //     }
        // }
    // }
}
