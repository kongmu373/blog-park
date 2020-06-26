FROM java:openjdk-8u111-alpine

RUN mkdir /app

WORKDIR /app

COPY target/blog-park-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "blog-park-1.0-SNAPSHOT.jar" ]
