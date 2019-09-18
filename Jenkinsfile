
VERSION_NUMBER = ''
appCode = 'dev'

node {
    stage('Pull Sources') {
        executeCommand("git config --global credential.helper cache")
        checkout scm
    }

    stage('Compile') {
        appgw 'clean classes testClasses'
        milestone()
    }

    stage('Unit Tests') {
        appgw 'test jacocoTestReport'
        junit '**/build/test-results/test/**/*.xml'
        milestone()
    }

    stage('Integration test') {
        appgw 'integrationTest'
        junit '**/build/test-results/integrationTest/**/*.xml'
    }

}

def appgw(goals) {
    dirgw appCode, goals
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