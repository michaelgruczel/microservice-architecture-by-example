FROM frolvlad/alpine-oraclejdk8:latest
MAINTAINER michael gruczel
EXPOSE 8080
ADD weather-server-0.1.0.jar /app/weather-server.jar

ENTRYPOINT java -jar /app/weather-server.jar --server.port=8080