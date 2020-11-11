FROM maven:3.6.3-openjdk-14-slim AS build

COPY settings.xml /usr/share/maven/conf/

COPY pom.xml pom.xml
COPY aos-api/pom.xml aos-api/pom.xml
COPY aos-base/pom.xml aos-base/pom.xml
COPY aos-database/pom.xml aos-database/pom.xml
COPY aos-model/pom.xml aos-model/pom.xml

RUN mvn dependency:go-offline package -B

## copy the pom and src code to the container
COPY aos-api/src aos-api/src
COPY aos-base/src aos-base/src
COPY aos-database/src aos-database/src
COPY aos-model/src aos-model/src

RUN mvn install

FROM openjdk:14-ea-jdk-alpine
USER root

RUN mkdir service

COPY --from=build /aos-base/target/ /service/

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.5.0/wait /wait

RUN chmod +x /wait

ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

EXPOSE 5005

CMD /wait && java --enable-preview -jar /service/aos-base-1.0-SNAPSHOT.jar -Xdebug