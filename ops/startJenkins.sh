#!/usr/bin/env bash

docker-compose -f jenkins/Docker/jenkins.yml up -d --build "$@"

echo "Jenkins should be available shortly on: http://localhost:8080"
read -p "Terminate Jenkins? [y] " close

while [ "$close" != "y" ]; do
    read -p "Terminate Jenkins? [y] " close
done

docker-compose -f jenkins/Docker/jenkins.yml down > /dev/null

echo "Jenkins terminated. https://bit.ly/2SzW1ha"
