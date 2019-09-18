VERSION_NUMBER = ''

node {
    stage('Pull Sources') {
        executeCommand("git config --global credential.helper cache")
        checkout scm
    }

    stage('Compile') {
        gw 'clean classes testClasses'
        milestone()
    }

    stage('Unit Tests') {
        gw 'test jacocoTestReport'
        junit '**/build/test-results/test/**/*.xml'
        milestone()
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