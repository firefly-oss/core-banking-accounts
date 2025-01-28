FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2) Runtime stage: minimal JDK
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/core-banking-accounts-web/target/core-banking-accounts.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]