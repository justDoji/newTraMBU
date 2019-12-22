FROM jenkins/jenkins:lts-alpine

USER root

RUN apk update \
&& apk add docker \
&& apk add py-pip \
&& pip install docker-compose~=1.23.0 \
&& rm -rf /var/cache/apk/*

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Djava.awt.headless=true -Dhudson.model.DirectoryBrowserSupport.CSP=\"sandbox allow-forms allow-scripts; default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src data:;\""

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt