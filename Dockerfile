# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-17 As Build
WORKDIR /app


COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests

  # 2. Run Stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/audit-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8089

ENTRYPOINT [ "java","-jar","/app/app.jar" ]