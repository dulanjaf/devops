pipeline {
    agent any

    environment {
        // Docker Hub image names
        FRONTEND_IMAGE = "dulanjafh/frontend"
        BACKEND_IMAGE  = "dulanjafh/backend"

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
                    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh '''
                            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                            docker push ${FRONTEND_IMAGE}:latest
                            docker push ${BACKEND_IMAGE}:latest
                            docker logout
                        '''
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
