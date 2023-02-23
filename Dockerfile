FROM maven:3.8.5-ibm-semeru-17-focal as build
COPY . /home/app
WORKDIR /home/app
RUN mvn clean package


FROM openjdk:19-jdk-alpine3.15 as production
EXPOSE 3001:3001
COPY --from=build /home/app/target/movie-catlog-service.jar /opt/service/movie-catlog-service.jar
WORKDIR /opt/service
ENTRYPOINT ["java","-jar","movie-catlog-service.jar"]
