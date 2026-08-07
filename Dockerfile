FROM bellsoft/liberica-openjdk-alpine:25
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]