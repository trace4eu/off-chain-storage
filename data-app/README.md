# OCS-DATA-APP

Java application built with Spring Boot framework

## BUILD CONTAINER
Setup the version (x.y.z accordingly) to the one that is in the [pom.xml](./pom.xml) (`version` xml tag)
```sh
docker build --no-cache -t ocs-data-app:x.y.z .
```

## RUN CONTAINER
The application needs a `config.properties` file. It is located within the container (internal path: `/app/config/config.properties`)
```sh
docke run -d -v {absolute path to the config.properties file}:/app/config/config.properties -p {hostPort}:8081 --name ocs-data-app ocs-data-app:x.y.z
```
For example:
```sh
docker run -d -v /data/trace4eu/off-chain-storage/config.properties:/app/config/config.properties -p 8081:8081 --name ocs-data-app ocs-data-app:0.4.9
```