FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -k -f http://apims/apims-cps/actuator/health || exit 1
ENTRYPOINT ["java","-jar","/app.jar"]
