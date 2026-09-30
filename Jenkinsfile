pipeline {
    agent any

    options {
        timeout(time: 45, unit: 'MINUTES')
    }

    environment {
        DOCKER_BUILDKIT = '1'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nermine-errokh/5ARCTIC8_NermineErrokh'
            }
        }

        stage('Build') {
            steps {
                dir('backend') { sh 'mvn clean compile -B' }
            }
        }

        stage('Tests') {
            steps {
                dir('backend') { sh 'mvn test -B' }
            }
            post {
                always { junit 'backend/target/surefire-reports/*.xml' }
            }
        }

        stage('SonarQube') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -B'
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
                dir('backend') { sh 'mvn package -DskipTests -B' }
            }
        }

        stage('Build Docker') {
            options { timeout(time: 15, unit: 'MINUTES') }
            steps {
                sh 'docker compose build'
            }
        }

        stage('Deploy (Compose)') {
            steps {
                sh 'docker compose down || true'
                sh 'docker rm -f gestion-projets-mysql gestion-projets-backend gestion-projets-frontend || true'
                sh 'docker compose up -d'
                sh 'docker compose ps'
            }
        }
    }

    post {
        success { echo 'Pipeline OK' }
        failure { echo 'Pipeline en échec' }
    }
}