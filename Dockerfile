FROM gradle:7.4-jdk-alpine as builder
WORKDIR /build

COPY build.gradle setting.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /build
RUN gradle build -x test --parallel

FROM openjdk:11.0-slim
WORKDIR /app

COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8081
ENTRYPOINT [
    "java",\
    "-jar",\
    "-Djava.security.egd=file:/dev/ ./urandom",\
    "-Dsun.net.inetaddr.ttl=0",
    "app.jar"\

]

CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=target/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]