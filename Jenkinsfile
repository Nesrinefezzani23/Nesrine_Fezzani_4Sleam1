pipeline {
    agent any

    environment {
        M2_HOME = "/usr/share/maven"
        PATH = "${env.M2_HOME}/bin:${env.PATH}"
        SONAR_HOST_URL = "http://localhost:9000"
    }

    stages {

        stage('Checkout') {
            steps {
                git url: 'https://github.com/Nesrinefezzani23/Nesrine_Fezzani_4Sleam1.git', branch: 'main'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests & Jacoco Coverage') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco execPattern: '**/target/jacoco.exec',
                           classPattern: '**/target/classes',
                           sourcePattern: '**/src/main/java'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=student-management \
                              -Dsonar.projectName='Student Management' \
                              -Dsonar.host.url=${SONAR_HOST_URL} \
                              -Dsonar.login=\${SONAR_TOKEN} \
                              -Dsonar.java.binaries=target/classes \
                              -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo "Pipeline finished"
        }
        success {
            echo "✅ Build succeeded! All tests passed."
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}