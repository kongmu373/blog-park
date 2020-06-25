FROM java:openjdk-8u111-alpine

RUN mkdir /app

WORKDIR /app

COPY target/blogVo-park-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "blogVo-park-1.0-SNAPSHOT.jar" ]
