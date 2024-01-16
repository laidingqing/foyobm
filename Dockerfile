FROM eclipse-temurin:11-alpine

WORKDIR /

COPY target/jidash* app-standalone.jar
EXPOSE 8080


CMD ["java", "-jar", "app-standalone.jar"]
