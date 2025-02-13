FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY /target/hw18-webflux-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
