# DEV

# OPS

This folder includes a dockerized unsecured Jenkins server.
It can help you to run a localized build pipeline. The Jenkins you will spin up locally will have no
configuration yet.
In order to get TRAMBU's pipeline running you will need to add a few credentials to the Jenkins instance,
and set up a multi-branch pipeline.

## Required credentials

### github
You will need github repository credentials for the pipeline to fetch source code.
We advise you to add a personal access token so you don't have to store your password in jenkins.
[[Setting up access tokens]](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line){:target="_blank"}.

Next, add a global credential to jenkins. Our pipeline assumes the credential has the id **git_token**.
Fill in your github user name, and put your newly created access token as the password.

### sonarcloud
Similarly to the github access token, the code analysis on sonarcloud assumes you have a sonarcloud access token.
[[Setting up access tokens]](https://docs.sonarqube.org/latest/user-guide/user-token/){:target="_blank"}.

Our pipeline assumes the sonar credential has the id **SonarCloud_Token**.
