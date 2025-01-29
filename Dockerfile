FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from the local workspace
COPY app.jar app.jar

# Expose if your Spring app runs on 8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
