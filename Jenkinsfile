pipeline {
    agent any

    environment {
        M2_HOME = "/usr/share/maven"
        PATH = "${env.M2_HOME}/bin:${env.PATH}"
        DOCKER_HUB_CREDENTIALS = 'docker-hub-credentials'
        DOCKER_IMAGE_NAME = 'user2312/student-management'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
        SONAR_HOST_URL = "http://host.docker.internal:9000"
    }

    stages {

        stage('Git Checkout') {
            steps {
                echo '=== Checking out code from GitHub ==='
                git url: 'https://github.com/Nesrinefezzani23/Nesrine_Fezzani_4Sleam1.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                echo '=== Building with Maven ==='
                sh 'mvn clean compile'
                sh 'mvn test jacoco:report'
                sh 'mvn package -DskipTests=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo '=== Running SonarQube Analysis ==='
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

        stage('Docker Build & Push') {
            steps {
                script {
                    echo '=== Building Docker Image ==='
                    sh """
                        docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest
                    """

                    echo '=== Pushing to Docker Hub ==='
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_HUB_CREDENTIALS}",
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                            docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                            docker push ${DOCKER_IMAGE_NAME}:latest
                            docker logout
                        """
                    }
                }
            }
        }

    }

    post {
        always {
            echo '=== Pipeline finished ==='
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
        success {
            echo '✅ Build succeeded with SonarQube analysis and Docker push!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}