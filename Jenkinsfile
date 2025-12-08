pipeline {
    agent any

    environment {
        M2_HOME = "/usr/share/maven"
        PATH = "${env.M2_HOME}/bin:${env.PATH}"
        DOCKER_HUB_CREDENTIALS = 'docker-hub-credentials'
        DOCKER_IMAGE_NAME = 'user2312/student-management'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
        KUBECONFIG = "/var/lib/jenkins/.kube/config"
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
                sh 'mvn test'
                sh 'mvn package -DskipTests=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
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

        stage('Kubernetes Deploy') {
            steps {
                script {
                    echo '=== Deploying to Kubernetes ==='
                    sh """
                        kubectl set image deployment/spring-app spring-app=${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -n devops
                        kubectl rollout status deployment/spring-app -n devops --timeout=5m
                        kubectl get pods -n devops
                    """
                }
            }
        }

        stage('Deploy MySQL & Spring Boot on K8s') {
            steps {
                script {
                    echo '=== Verifying MySQL and Spring Boot Deployment ==='
                    sh """
                        kubectl get pods -l app=mysql -n devops
                        kubectl get pods -l app=spring-app -n devops
                        kubectl get svc -n devops
                        echo "=== Application URL ==="
                        echo "Run: minikube service spring-service -n devops --url"
                    """
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
            echo '✅ Build succeeded and deployed to Kubernetes!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}