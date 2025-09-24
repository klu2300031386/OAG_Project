pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/klu2300031386/OAG_Project.git'
            }
        }

        stage('Frontend Build') {
            steps {
                dir('OAG_Frontend') {
                    bat """
                        echo Installing frontend dependencies...
                        call npm install
                        echo Building frontend...
                        call npm run build
                    """
                }
            }
        }

        stage('Backend Build') {
            steps {
                dir('OAG_Backend') {
                    bat """
                        echo Building backend with Maven...
                        call "${tool 'Maven_3_9_11'}/bin/mvn" clean package -DskipTests
                    """
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'OAG_Frontend/dist/**', fingerprint: true
                archiveArtifacts artifacts: 'OAG_Backend/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo "✅ Frontend & Backend build successful."
        }
        failure {
            echo "❌ Build failed."
        }
    }
}
