
VERSION_NUMBER = ''
appCode = 'dev'
intrastructure = 'dev/trambu-infrastructure'

node {
    stage('Pull Sources') {
        executeCommand("git config --global credential.helper cache")
        checkout scm
    }

    stage('Compile') {
        dirgw appCode, 'clean classes testClasses'
        milestone()
    }

    stage('Unit Tests') {
        dirgw appCode,'test jacocoTestReport'
        junit '**/build/test-results/test/**/*.xml'
        milestone()
    }

    stage('Integration test') {
        dirgw intrastructure, 'integrationTest'
        junit '**/build/test-results/integrationTest/**/*.xml'
    }

}
def dirgw(directory, goals) {
    dir(directory) {
        gw(goals)
    }
}

def gw(goals) {
    if (isUnix()) {
        sh "./gradlew $goals -PapplicationVersion=${VERSION_NUMBER}"
    } else {
        bat 'gradlew.bat ' + goals
    }
}

def executeCommand(command) {
    sh(script: command, returnStdout: true).trim()
}