pipeline {
    agent any

    environment {
        M2_HOME = "/usr/share/maven"
        PATH = "${env.M2_HOME}/bin:${env.PATH}"
        SONAR_HOST_URL = "http://localhost:9000" // Adapter selon votre config
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

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    // Publier les résultats des tests
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=spring-app \
                              -Dsonar.projectName='Spring Application' \
                              -Dsonar.host.url=${SONAR_HOST_URL} \
                              -Dsonar.java.coveragePlugin=jacoco \
                              -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
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
            // Publier le rapport Jacoco
            jacoco()
        }
        success {
            echo "Build succeeded! Quality gates passed."
        }
        failure {
            echo "Build failed!"
        }
    }
}