FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mkdir -p /root/.m2 \
    && mkdir /root/.m2/repository
# Copy maven settings, containing repository configurations
COPY .mvn/settings.xml /root/.m2
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/review-service.jar /usr/local/lib/review-service.jar
EXPOSE 8080
EXPOSE 9090
ENTRYPOINT ["java","-jar","/usr/local/lib/review-service.jar"]