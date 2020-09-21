FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
RUN apk --no-cache add curl
COPY build/libs/*-all.jar hwk-server.jar
CMD java ${JAVA_OPTS} -jar hwk-server.jar
