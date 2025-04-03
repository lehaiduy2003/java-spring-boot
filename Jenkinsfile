/* Requires the Docker Pipeline plugin */
pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-21-alpine'
            args '-v /var/jenkins_home/.m2:/root/.m2' // Mount Maven Cache
        }
    }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}