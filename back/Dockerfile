FROM maven:3.9.4 as builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:17-ea-slim-buster

WORKDIR /app

COPY --from=builder /app/target/slots-0.0.1-SNAPSHOT.jar slots-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "slots-0.0.1-SNAPSHOT.jar"]
