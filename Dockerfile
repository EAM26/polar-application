FROM amazoncorretto:21

WORKDIR /app

COPY target/polar-app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]