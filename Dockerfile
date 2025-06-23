FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . /app

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/variant-annotator-0.0.1-SNAPSHOT.jar"]
