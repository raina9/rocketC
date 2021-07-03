#FROM openjdk:8-jre-alpine3.9
FROM openjdk:8-jdk-alpine
# Refer to Maven build -> finalName
ARG JAR_FILE=/weblearnex-master/target/weblearnex-master.jar

# cd /opt/app
#WORKDIR /opt/app
#WORKDIR /Users/apple/Desktop/jar
# cp target/spring-boot-web.jar /opt/app/app.jar

COPY ${JAR_FILE} weblearnex-master.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","weblearnex-master.jar"]
