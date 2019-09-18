VERSION_NUMBER = ''

node {
    stage('Pull Sources') {
        checkout scm
    }

    stage('Compile') {
        gw 'clean classes testClasses'
        milestone
    }

    stage('Unit Tests') {
        gw test jacocoTestReport
        junit '**/build/test-results/test/**/*.xml'
        milestone
    }

}

def gw(goals) {
    if (isUnix()) {
        sh "./gradlew $goals -PapplicationVersion=${VERSION_NUMBER}"
    } else {
        bat 'gradlew.bat ' + goals
    }
}