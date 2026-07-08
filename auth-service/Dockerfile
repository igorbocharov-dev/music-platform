# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -pl auth-service -am package -DskipTests

#Run stage
FROM eclipse-temurin:21-jre
WORKDIR /opt/auth-service
COPY --from=build /build/auth-service/target/auth-service-*.jar authapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authapp.jar"]