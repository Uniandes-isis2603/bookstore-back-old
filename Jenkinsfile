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
      stage('ARCC') {
         // Run arcc analysis
         steps {
            script {
               docker.image('arcc-tools-isis2603:latest').inside('-e ARCHID_TOKEN=${ARCHID_TOKEN}') {
                  sh '''
                     java -version
                     java -cp /eclipse/plugins/org.eclipse.equinox.launcher_1.5.700.v20200207-2156.jar org.eclipse.equinox.launcher.Main -application co.edu.uniandes.archtoring.archtoring --full
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
