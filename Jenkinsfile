pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "siwarhmedi/events-project:latest"
        DOCKER_CREDENTIALS = "docker-hub-credentials"
        NEXUS_REPO = "http://localhost:8082/#browse/browse:maven-releases"
        NEXUS_CREDENTIALS = "nexus-credentials"
    }

    stages {
        // Étape 1: Récupération du code source
        stage('Checkout Git') {
            steps {
                git branch: 'master', 
                url: 'https://github.com/SiwarHmedii/Devops.git'
            }
        }

        // Étape 2: Compilation du projet
        stage('Compilation') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        // Étape 3: Tests unitaires
        stage('Tests Unitaires') {
            steps {
                sh 'mvn test'
            }
        }

        // Étape 4: Analyse de qualité de code
        stage('Qualité de Code') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    withCredentials([string(credentialsId: 'SONARQUBE_TOKEN', variable: 'SONAR_TOKEN')]) {
                        sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                    }
                }
            }
        }

        // Étape 5: Préparation du livrable
        stage('Préparation Livrable') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // Étape 6: Déploiement sur Nexus
        stage('Déploiement Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: NEXUS_CREDENTIALS,
                    usernameVariable: 'NEXUS_USER',
                    passwordVariable: 'NEXUS_PASS'
                )]) {
                    sh """
                    curl -u $NEXUS_USER:$NEXUS_PASS \
                    --upload-file target/eventsProject-*.jar \
                    $NEXUS_REPO
                    """
                }
            }
        }

        // Étape 7: Construction de l'image Docker
        stage('Construction Image Docker') {
            steps {
                script {
                    dockerImage = docker.build(DOCKER_IMAGE)
                }
            }
        }

        // Étape 8: Publication sur DockerHub
        stage('Publication DockerHub') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_CREDENTIALS) {
                        dockerImage.push()
                    }
                }
            }
        }

        // Étape 9: Déploiement avec Docker Compose
        stage('Déploiement Final') {
            steps {
                sh '''
                if [ -f docker-compose.yml ]; then
                  docker-compose down
                  docker-compose up -d --force-recreate
                else
                  echo "ERREUR: Fichier docker-compose.yml manquant!"
                  exit 1
                fi
                '''
            }
        }
    }

    post {
        success {
            echo '✅ SUCCÈS: Pipeline exécutée avec succès!'
            //slackSend channel: '#devops', message: 'Déploiement réussi ✅'  // Optionnel
        }
        failure {
            echo '❌ ÉCHEC: Vérifiez les erreurs dans les logs!'
            //slackSend channel: '#devops', message: 'Échec du déploiement ❌'  // Optionnel
        }
    }
}
