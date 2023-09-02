FROM amazoncorretto:11-alpine-jdk
COPY target/notification-service-1.0-SNAPSHOT.jar notification-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/notification-service-1.0.0.jar"]