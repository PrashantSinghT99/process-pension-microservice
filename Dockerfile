FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 7004
ADD target/*.jar process-pension-microservice.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /process-pension-microservice.jar" ]