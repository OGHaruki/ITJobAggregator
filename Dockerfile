# Stage 1
# initialize build and set base image for first stage
FROM maven:3.8.5-amazoncorretto-17 AS stage1

# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# set working directory
WORKDIR /app

# copy the pom.xml file
COPY pom.xml .

# go-offline using the pom.xml
RUN mvn dependency:go-offline

# copy the source code
COPY src src

# compile the source code and package it in a jar file
RUN mvn clean install -Dmaven.test.skip=true

# Stage 2
# set base image for second stage
FROM amazoncorretto:17

# set working directory
WORKDIR /app

# copy the jar file from the first stage
COPY --from=stage1 /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]