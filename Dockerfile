#
# Build stage
#
FROM maven:3.8.6-openjdk-18-slim AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:18-jdk-slim
COPY --from=build /target/AlmatapBackend-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]