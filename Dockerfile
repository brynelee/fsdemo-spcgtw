# used for hub.docker.com automation build (CI)
FROM maven:3.6.3-jdk-8
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.1.1:go-offline
COPY . .
RUN mvn -B -e -T 1C -Dmaven.test.skip=true package

# package without maven
# FROM openjdk:8-jdk-alpine
# RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
# use FROM bryne/spmiabaseimage image to fasten the build process
# FROM bryne/spmiabaseimage
FROM xdorg1/javacommon
WORKDIR /usr/src/app
COPY --from=0 /usr/src/app/target/*.jar .
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh