FROM eclipse-temurin:21-jdk-alpine AS builder

RUN apk add --no-cache git ca-certificates && \
    update-ca-certificates

WORKDIR /app

COPY gradlew .
RUN chmod +x gradlew
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# Download dependencies (cached layer)
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}
RUN ./gradlew dependencies --no-daemon

# Build
COPY src src
RUN ./gradlew build -Dquarkus.package.type=fast-jar -x test --no-daemon

# Runtime
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl \
    && addgroup -S quarkus \
    && adduser -S quarkus -G quarkus

WORKDIR /deployments
RUN chown quarkus:quarkus /deployments

COPY --from=builder --chown=quarkus:quarkus /app/build/quarkus-app/lib/ ./lib/
COPY --from=builder --chown=quarkus:quarkus /app/build/quarkus-app/quarkus/ ./quarkus/
COPY --from=builder --chown=quarkus:quarkus /app/build/quarkus-app/app/ ./app/
COPY --from=builder --chown=quarkus:quarkus /app/build/quarkus-app/*.jar ./

USER quarkus
EXPOSE 8080
HEALTHCHECK NONE

ENV QUARKUS_PROFILE=prod
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
CMD ["java", "-jar", "quarkus-run.jar"]
