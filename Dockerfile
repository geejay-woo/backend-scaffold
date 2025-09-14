FROM openjdk:11-jre-slim
COPY build/libs/backend-scaffold-0.0.1-SNAPSHOT.jar ./
CMD java -jar backend-scaffold-0.0.1-SNAPSHOT.jar