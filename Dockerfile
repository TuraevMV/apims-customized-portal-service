FROM maven:3.6.3-openjdk-8 as jdk

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=jdk /app/target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -k -f http://apims/apims-cps/actuator/health || exit 1
ENTRYPOINT ["java","-jar","/app.jar"]
