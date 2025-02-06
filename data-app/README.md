# OCS-DATA-APP

Java application built with Spring Boot framework

## CONFIGURATION PARAMETERS
This an example (with dev values) of the `config.properties` file:

```
database.url=cass-dev.trace4eu.eu:9042
database.username=
database.password=
database.hostname=cass-dev.trace4eu.eu
database.dbname=ocs
database.port=9042
database.datacenter=GovPart1
database.clustername=Trace4eu
database.connection.max_requests=1024
database.connection.pool_remotesize=1
database.connection.pool_localsize=1
```

## RUN APPLICATION
You need to compile the application using maven for generating the artifact (.jar file).  
The command `mvn clean package` will generate the artifact.  
It will include the version that is defined in the [pom.xml](pom.xml) file. The jar file will be place in `/target`.  
For example if version `0.4.9` is defined, you can run the application with this command:
```
java -jar target/ocs-data-app-0.4.9.jar -i <path to the properties file> --server.port=8081
```

## BUILD CONTAINER
Setup the version (x.y.z accordingly) to the one that is in the [pom.xml](./pom.xml) (`version` xml tag)
```sh
docker build --no-cache -t ocs-data-app:x.y.z .
```

## RUN CONTAINER
The application needs a `config.properties` file. It is located within the container (internal path: `/app/config/config.properties`) so at the time of running the container you can map a local properties file into the container.
```sh
docker run -d -v {absolute path to the config.properties file}:/app/config/config.properties -p {hostPort}:8081 --name ocs-data-app ocs-data-app:x.y.z
```
For example:
```sh
docker run -d -v /data/trace4eu/off-chain-storage/config.properties:/app/config/config.properties -p 8081:8081 --name ocs-data-app ocs-data-app:0.4.9
```
