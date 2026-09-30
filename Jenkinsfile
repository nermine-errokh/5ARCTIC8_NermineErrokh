pipeline {
    agent any

    stages {
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nermine-errokh/5ARCTIC8_NermineErrokh'
            }
        }

        stage('Build') {
            steps {
                dir('backend') { sh 'mvn clean compile' }
            }
        }

        stage('Tests') {
            steps {
                dir('backend') { sh 'mvn test' }
            }
            post {
                always { junit 'backend/target/surefire-reports/*.xml' }
            }
        }
                stage('SonarQube') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Package') {
            steps {
                dir('backend') { sh 'mvn package -DskipTests' }
            }
        }
                stage('Build Docker') {
            steps {
                sh 'docker build -t nomprenom_5ARCTIC8_gestionprojets-backend:latest ./backend'
                sh 'docker build -t nomprenom_5ARCTIC8_gestionprojets-frontend:latest ./frontend'
            }
        }

        stage('Deploy (Compose)') {
            steps {
                sh 'docker compose down || true'
                sh 'docker compose up -d --build'
                sh 'docker compose ps'
            }
        }
    }
}