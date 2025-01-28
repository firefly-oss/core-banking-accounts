FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY core-banking-accounts-web-*.jar app.jar

USER 1001

EXPOSE 8080
EXPOSE 8081
EXPOSE 9090

CMD [ "java", "-jar", "app.jar" ]