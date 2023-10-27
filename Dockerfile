FROM gradle:8.4.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble --no-daemon

FROM openjdk:17-alpine

EXPOSE 8181

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/gaishnik-1.0.0.jar /app/gaishnik-1.0.0.jar

ENTRYPOINT ["java", "-jar", "/app/gaishnik-1.0.0.jar"]
