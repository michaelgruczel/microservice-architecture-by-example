FROM frolvlad/alpine-oraclejdk8:latest
MAINTAINER michael gruczel
EXPOSE 8080
ADD concert-server-0.1.0.jar /app/concert-server.jar

ENTRYPOINT java -jar /app/concert-server.jar --server.port=8080