FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY /app/core-banking-accounts-web/target/core-banking-accounts.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]