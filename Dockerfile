FROM ubuntu:16.04

ENV DT_AGENT_PATH=
WORKDIR /app
VOLUME /tmp
ADD . /app
RUN apt-get update
RUN apt-get install --yes imagemagick
RUN apt-get install --yes openjdk-8-jdk gradle
RUN chmod +x start.sh &&     /app/gradlew build
EXPOSE 9000
ENTRYPOINT ["./start.sh"]