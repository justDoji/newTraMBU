
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

    stage('Build docker image') {
        dockerHub('buildDockerImage')
        echo 'Docker image built and ready to roll!'
    }

    stage('Spin up containers') {

    }
    stage('I MADE IT, CJ! -- Big Smoke') {
        devgw('-PrequestUrl=http://bingoapp:9999 smokeTest')
        junit '**/build/test-results/smokeTest/**/*.xml'
    }

}

// Build utility methods

private void dockerHub(tasks) {
    dir('dev') {
        withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'dockerHubUsername', passwordVariable: 'dockerHubPassword')]) {
            sh "./gradlew $tasks -PapplicationVersion=$VERSION_NUMBER -PdockerHubUsername=$dockerHubUsername -PdockerHubPassword=$dockerHubPassword"
        }
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