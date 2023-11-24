FROM eclipse-temurin:17.0.9_9-jre-ubi9-minimal
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]