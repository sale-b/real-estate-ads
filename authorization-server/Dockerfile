FROM amazoncorretto:11-alpine-jdk
RUN apk --no-cache add curl
COPY target/authorization-server-1.0-SNAPSHOT.jar auth-server-1.0.0.jar
ENTRYPOINT ["java","-jar","/auth-server-1.0.0.jar"]