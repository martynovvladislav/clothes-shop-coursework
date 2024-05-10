FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/clothes_shop-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]