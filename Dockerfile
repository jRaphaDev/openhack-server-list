FROM openjdk:8

ADD target/server-list-1.0.0.jar server-list-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-list-1.0.0.jar"]