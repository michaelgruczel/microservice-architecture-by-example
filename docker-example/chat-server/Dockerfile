FROM frolvlad/alpine-oraclejdk8:latest
MAINTAINER michael gruczel
EXPOSE 8080
ADD chat-service-0.0.1 /app/chat-server.jar

ENTRYPOINT java -jar /app/chat-server.jar --server.port=8080