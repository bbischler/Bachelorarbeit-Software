FROM fabric8/java-alpine-openjdk8-jre
COPY target/lib/* /deployments/lib/
COPY target/quarkus-1.0.0-SNAPSHOT-runner.jar  /deployments/app.jar
ENTRYPOINT [ "/deployments/run-java.sh" ]
EXPOSE 8080

# mvn package
# docker build  -t bischler/pp3-quarkus .
# docker run -i --rm -p 8080:8080 bischler/pp3-quarkus
# docker tag 2a2b8eae9b28 bischler/pp3-quarkus
# docker push bischler/pp3-quarkus
# docker system prune -a --volumes
# docker-compose up --build