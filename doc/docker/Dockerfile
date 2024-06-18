FROM openjdk:latest
ARG SERVERPORT=8081
ARG JARFILE=./file-service-complete-0.0.1.jar
ARG JARVERSION="0.1"
ENV SERVERPORT=${SERVERPORT}
RUN groupadd  trace4eu
RUN useradd -g trace4eu trace4eu
USER trace4eu:trace4eu
COPY ${JARFILE} /start.jar
EXPOSE ${SERVERPORT}
ENTRYPOINT java -jar /start.jar --server.port=$SERVERPORT
