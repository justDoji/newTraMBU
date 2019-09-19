
VERSION_NUMBER = ''
appCode = 'dev'

node {
    stage('Pull Sources') {
        executeCommand("git config --global credential.helper cache")
        checkout scm
        regularBuildVersionNumber()
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
        appgw('dockerComposeUp')
    }

    stage('I MADE IT, CJ! -- Big Smoke') {
        appgw('-PrequestUrl=http://192.168.99.100:8888/index.xhtml smokeTest')
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

// Versioning
def regularBuildVersionNumber() {
    def properties = readProperties file: 'dev/gradle.properties'
    def versionPrefix = properties['versionPrefix']
    if (versionPrefix == null || versionPrefix == '') {
        error 'No versionPrefix property found in the gradle.properties file. Please make sure this property is present with a correct value e.g. internalVersionPrefix=1.0'
    }
    def patchVersion = patchVersion(versionPrefix)
    VERSION_NUMBER = "${versionPrefix}.${patchVersion}"
    println "Building with version number: ${VERSION_NUMBER}"
}

def patchVersion(versionPrefix) {
    def patchVersion
    try {
        executeCommand("git fetch --tags")
        patchVersion = executeCommand("git rev-list --count ${versionPrefix}.0...HEAD")
    } catch (e) {
        patchVersion = '0'
    }
    println "Patch version is [${patchVersion}]"
    return patchVersion
}