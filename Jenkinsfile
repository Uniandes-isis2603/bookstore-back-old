pipeline {
   agent any 
   environment {
      GIT_REPO = 'bookstore-back'
      GIT_CREDENTIAL_ID = 'de5cd571-10da-4034-8ba8-af99beef4b14'
      ARCHID_TOKEN = credentials('041703df-dd96-47c3-97b1-b7fbf12069d5')
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
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
   }
}

