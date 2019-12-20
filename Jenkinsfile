
VERSION_NUMBER = ''
appCode = 'dev'
timeTracking = appCode + '/timetracking'
zulma = appCode + "/aunt-zulma"

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

    stage('Build docker images') {
        appgw('timetracking:buildDockerImage')
        echo 'Docker image built and ready to roll!'
    }

    stage('Aunt Zulma - Spin up containers') {
        appgw('aunt-zulma:dockerComposeUp')
        echo 'TimeTracking container Running!'
    }

    stage("Aunt Zulma - Acceptance testing") {
      appgw('aunt-zulma:clean aunt-zulma:test')
      appgw('aunt-zulma:aggregate')
      junit zulma + '/build/test-results/**/*.xml'

      publishHTML(target: [
              reportName : 'Serenity',
              reportDir:   zulma+'target/site/serenity',
              reportFiles: 'index.html',
              keepAll:     true,
              alwaysLinkToLastBuild: true,
              allowMissing: false
          ])

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

private void sonar(tasks) {
    dir('dev') {
        withCredentials([usernamePassword(credentialsId: 'SonarCloud_Token', usernameVariable: 'sonarUsername', passwordVariable: 'sonarPassword')]) {
            sh "./gradlew $tasks -PapplicationVersion=$VERSION_NUMBER -PsonarLogin=$sonarPassword"
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