FROM openjdk:9-jre-slim
COPY target/tesla-0.0.1-SNAPSHOT.jar .
CMD exec /usr/bin/java -Xmx400m -Xms400m -jar tesla-0.0.1-SNAPSHOT.jar
EXPOSE 8082

# mvn package
# docker-compose up --build
