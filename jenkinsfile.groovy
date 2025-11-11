pipeline {
    agent any

    environment {
        // Docker Hub image names
        FRONTEND_IMAGE = "dulanjaf/frontend"
        BACKEND_IMAGE  = "dulanjaf/backend"

        // Git repository
        GIT_REPO = "https://github.com/dulanjaf/devops.git"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                script {
                    sh "docker build -t ${FRONTEND_IMAGE}:latest -f frontend/Dockerfile frontend"
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {
                    sh "docker build -t ${BACKEND_IMAGE}:latest -f backend/Dockerfile backend"
                }
            }
        }

        stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                    // Uses Jenkins credentials with ID 'dockerhub'
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
                        sh "docker push ${FRONTEND_IMAGE}:latest"
                        sh "docker push ${BACKEND_IMAGE}:latest"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed — cleaning up workspace.'
        }
    }
}
