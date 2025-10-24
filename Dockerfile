FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . /app

RUN ./mvnw package

EXPOSE 8080

CMD ["java", "-jar", "target/ecoplate-0.0.1-SNAPSHOT.jar"]