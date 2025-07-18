FROM openjdk:21-jdk-slim as final

RUN mkdir -p /opt/app/config
RUN mkdir -p /opt/app/public

VOLUME /tmp
VOLUME /opt/app/config
VOLUME /opt/app/logs
VOLUME /opt/app/public

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

ARG JAR_FILE=target/just-reader-monitor.jar
COPY ${JAR_FILE} /opt/app/just-reader-monitor.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/opt/app/just-reader-monitor.jar","--spring.profiles.active=prod"]
