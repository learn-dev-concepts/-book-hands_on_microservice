FROM eclipse-temurin:17-jdk

EXPOSE 8080

ADD ./build/libs/product-composite-service-1.0.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]