# Use Java 21 (adjust if you prefer a different base)
FROM eclipse-temurin:21-jdk

# Create a directory inside the container
WORKDIR /app

# Copy only the final JAR from the web module
COPY core-banking-accounts-web/target/core-banking-accounts.jar app.jar

# If your Spring Boot app listens on 8080, expose it (adjust if needed)
EXPOSE 8080

# Run the jar
CMD ["java","-jar","app.jar"]