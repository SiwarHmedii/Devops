pipeline {
    agent any

    stages {
        // Stage 1: Checkout code from GitHub
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/SiwarHmedii/Devops.git'
            }
        }

        // Stage 2: Build the project with Maven
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        // Stage 3: Run unit tests
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        // Stage 4: Analyze code quality with SonarQube
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') { // Use the SonarQube server name configured in Jenkins
                    sh 'mvn sonar:sonar'
                }
            }
        }

        // Stage 5: Build the Docker image
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("Siwarhmedi/events-project:latest")
                }
            }
        }

        // Stage 6: Push the Docker image to Docker Hub
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dckr_pat_mJEN_sHNNnADDOVDMmn1yZ1QaWg') {
                        dockerImage.push()
                    }
                }
            }
        }

        // Stage 7: Deploy using Docker Compose
        stage('Deploy') {
            steps {
                sh 'docker-compose down && docker-compose up -d'
            }
        }
    }

    // Post-build actions (e.g., send notifications)
    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
