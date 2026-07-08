# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B -pl music-service -am package -DskipTests

#Run stage
FROM eclipse-temurin:21-jre
WORKDIR /opt/music-service
COPY --from=build /build/music-service/target/music-service-*.jar musapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "musapp.jar"]

