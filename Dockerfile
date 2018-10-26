FROM openjdk:8

RUN mkdir /app
RUN mkdir -p /root/.kube
WORKDIR /app

COPY ./config /root/.kube/config
COPY . .

ADD target/server-list-1.0.0.jar server-list-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server-list-1.0.0.jar"]