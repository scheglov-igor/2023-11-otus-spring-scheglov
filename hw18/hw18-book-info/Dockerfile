FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY /target/hw18-book-info-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
