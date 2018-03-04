def buildName = ''
def version = ''

withCredentials([usernamePassword(credentialsId: 'gitID', passwordVariable: 'git_password', usernameVariable: 'git_user')]) {
// clone git repository
node('master') {
    stage('Git clone') { 
        dir('task6'){
             git branch: 'task6', url: 'https://github.com/AlexandrKowalev/DevOps_training.git'
        }
        
    }
// build and increment version project
   stage('Build and incVersion') { 
        dir('task6'){
             sh '/opt/gradle/gradle-3.4.1/bin/gradle incrementVersion'
             sh '/opt/gradle/gradle-3.4.1/bin/gradle build'
        }
   }
   
// auto created package of build for Nexus
   stage('read version of buid'){
        dir('task6/build/libs'){
            String snapshot="libs"
            new File(pwd()).eachFileRecurse{file->
            buildName=file.name.replace(".war","")
        }
    }
        
        dir('task6'){
            Properties properties = new Properties()
            sh 'ls'
            File propertiesFile = new File(pwd()+'/gradle.properties')
            def props = readProperties  file: pwd()+'/gradle.properties'
            version = props.version+'.'+props.buildVersion
          
        }
   }
// upload to nexus repository
        stage('Upload to nexus') { // upload to nexus repository
            dir('task6'){
                sh "curl -v -u admin:admin123 --upload-file build/libs/${buildName}.war http://localhost:8081/nexus/content/repositories/snapshots/${buildName}/${version}/${buildName}.war"
                
            }
        }
    }
//loading warfile on tomcats
    node('tomcat1'){
        stage('dowload warfile tomcat1'){
            httpRequest outputFile: "${buildName}.war", responseHandle: 'NONE', url: "http://192.168.0.10:8081/nexus/service/local/repositories/snapshots/content/${buildName}/${version}/${buildName}.war"
        }
        stage('upgrading tomcat1'){
            httpRequest responseHandle: 'NONE', url: 'http://192.168.0.10/jkmanager?cmd=update&from=list&w=lb&sw=tomcat1&vwa=1'
            sh "cp ${buildName}.war /usr/share/tomcat/webapps/"
            sleep 20
            httpRequest responseHandle: 'NONE', url: 'http://192.168.0.10/jkmanager?cmd=update&from=list&w=lb&sw=tomcat1&vwa=0'
        }
    }
    node('tomcat2'){
        stage('dowload warfile tomcat2'){
            httpRequest outputFile: "${buildName}.war", responseHandle: 'NONE', url: "http://192.168.0.10:8081/nexus/service/local/repositories/snapshots/content/${buildName}/${version}/${buildName}.war"
        }
        stage('upgrading tomcat2'){
            httpRequest responseHandle: 'NONE', url: 'http://192.168.0.10/jkmanager?cmd=update&from=list&w=lb&sw=tomcat1&vwa=1'
            sh "cp ${buildName}.war /usr/share/tomcat/webapps/"
            sleep 20
            httpRequest responseHandle: 'NONE', url: 'http://192.168.0.10/jkmanager?cmd=update&from=list&w=lb&sw=tomcat1&vwa=0'
        }
    }
    node('master') {
        stage('Git commit & push') {
             dir('task6') {
                sh 'git add *'
                sh 'git config --global user.email "KowalevAlexsandr@yandex.ru"'
                sh 'git config --global user.name "AlexandrKowalev"'
                sh "git commit -m 'new version ${version}'"
                sh "git push https://${git_user}:${git_password}@github.com/AlexandrKowalev/DevOps_training.git --all"
                sh 'git checkout master'
                sh 'git merge task6'
                sh "git tag -a v${version} -m 'version ${version}'"
                sh 'git push https://${git_user}:${git_password}@github.com/AlexandrKowalev/DevOps_training.git --tags'
                sh 'git push https://${git_user}:${git_password}@github.com/AlexandrKowalev/DevOps_training.git --all'
            } 
        }
    }
    
}