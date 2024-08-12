pipeline {
    agent any
    environment{
        DOCKERHUB_CREDENTIALS=credentials('ae7fd4fb-c1c5-407a-b965-9c40b6631e77')
    }
    stages {
        stage('git') {
            agent{
                label 'kubemaster'
            }
            steps {
                git 'https://github.com/RahulDeswalOne/website.git'
            }
        }
        stage('Docker') {
            agent{
                label 'kubemaster'
            }
            steps {
                sh 'sudo docker build . -t rahuldeswalone/websiteimage'
                sh 'sudo echo $DOCKERHUB_CREDENTIALS_PSW | sudo docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'sudo docker push rahuldeswalone/websiteimage'
            }   
        }
        stage('Kubernetes') {
            agent{
                label 'kubemaster'
            }
            steps {
                sh 'kubectl apply -f deploy.yml'
                sh 'kubectl apply -f svc.yml'
            }
        }
    }
}