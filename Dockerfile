FROM gradle:7.4-jdk-alpine as builder
WORKDIR /build
COPY build.gradle setting.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=target/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]