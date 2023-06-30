pipeline {
   agent any 
   environment {
      GIT_REPO = 'bookstore-back'
      GIT_CREDENTIAL_ID = '7c21addc-0cbf-4f2e-9bd8-eced479c56c6'
      SONARQUBE_URL = 'http://172.24.101.209:8082/sonar-isis2603'
   }
   stages {
      stage('Checkout') { 
         steps {
            scmSkip(deleteBuild: true, skipPattern:'.*\\[ci-skip\\].*')

            git branch: 'master', 
               credentialsId: env.GIT_CREDENTIAL_ID,
               url: 'https://github.com/Uniandes-isis2603/' + env.GIT_REPO
         }
      }
      stage('Statistical analysis') { 
         steps {
            withCredentials([usernamePassword(credentialsId: env.GIT_CREDENTIAL_ID, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
               sh 'mkdir -p code-analyzer-report'
               sh """ curl --request POST --url https://code-analyzer.virtual.uniandes.edu.co/analyze --header "Content-Type: application/json" --data '{"repo_url":"git@github.com:Uniandes-isis2603/bookstore-front.git", "access_token": "${GIT_PASSWORD}" }' > code-analyzer-report/index.html """   
            }
            publishHTML (target: [
               allowMissing: false,
               alwaysLinkToLastBuild: false,
               keepAll: true,
               reportDir: 'code-analyzer-report',
               reportFiles: 'index.html',
               reportName: "GitInspector"
            ])
         }
      }          
      stage('Build') {
         // Build artifacts
         steps {
            script {
               docker.image('springtools-isis2603:latest').inside('-v ${WORKSPACE}/maven:/root/.m2') {
                  sh '''
                     java -version
                     ./mvnw clean install
                  '''
               }
            }
         }
      }
      stage('Testing') {
         // Run unit tests
         steps {
            script {
               docker.image('springtools-isis2603:latest').inside('-v ${WORKSPACE}/maven:/root/.m2') {                  
                  sh '''
                     ./mvnw clean test   
                  '''
               }
            }
         }
      }
      stage('Static Analysis') {
         // Run static analysis
         steps {
            script {
               docker.image('springtools-isis2603:latest').inside('-v ${WORKSPACE}/maven:/root/.m2') {
                  sh '''
                     ./mvnw clean verify sonar:sonar -Dsonar.host.url=${SONARQUBE_URL}   
                  '''
               }
            }
         }
      }
   }
   post {
      always {
        cleanWs()
        deleteDir() 
        dir("${env.GIT_REPO}@tmp") {
          deleteDir()
        }
      }
   }
}
