FROM maven:3.6.0-jdk-11-slim AS build
## Copy api module src and pom
COPY api/src /home/app/api/src
COPY api/pom.xml /home/app/api
## Copy server module src and pom
COPY server/src /home/app/server/src
COPY server/pom.xml /home/app/server
# Copy parent pom
COPY pom.xml /home/app
RUN mkdir -p /root/.m2 \
    && mkdir /root/.m2/repository
# Copy maven settings, containing repository configurations
# COPY .mvn/settings.xml /root/.m2
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/server/target/review-service.jar /usr/local/lib/review-service.jar
EXPOSE 8080
EXPOSE 9090
ENTRYPOINT ["java","-jar","/usr/local/lib/review-service.jar"]