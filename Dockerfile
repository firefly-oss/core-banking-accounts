FROM eclipse-temurin:21-jdk

COPY core-banking-accounts-web-*.jar /run/core-banking-accounts-web-.jar

USER 1001

EXPOSE 8080
EXPOSE 8081
EXPOSE 9090

CMD [ "java","-jar", "run/core-banking-accounts-web-.jar"]