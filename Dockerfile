FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]