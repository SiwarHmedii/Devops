pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/SiwarHmedii/Devops.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploying the application..."'
            }
        }
    }
}
