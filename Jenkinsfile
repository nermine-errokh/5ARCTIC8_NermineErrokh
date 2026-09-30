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

        stage('Package') {
            steps {
                dir('backend') { sh 'mvn package -DskipTests' }
            }
        }
    }
}