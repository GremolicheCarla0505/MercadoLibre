FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos el JAR que ya generaste con Gradle
COPY build/libs/mutants-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
