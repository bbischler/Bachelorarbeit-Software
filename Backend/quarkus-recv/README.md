# PP3 Car-Backend
Dieser Code stellt ein Backend da, welches Sensor-Daten über eine REST-Schnittstelle empfängt und diese in einer influxDB speichert. Der Code ist in einem Docker-Image bereitgestellt. In diesem Image ist ebenfalls die influxDB und Chronograf als Dashboard für die Datenbank enthalten.

### Building
Um die App local zu starten den Code clonen und folgenden Befehl ausführen (startet das Backend, influxDB und Chronograf):
```sh
$ docker-compose up --build
```
Da das Backend in einem Docker-Image läuft kann es auch aus docker hub gepullt werden
```sh
$ docker pull bischler/pp3-quarkus
```

 Um den Container zu starten folgenden Befehl ausführen (startet das Backend, influxDB und Chronograf)
 ```sh
$ docker run -i --rm -p 8080:8080 bischler/pp3-quarkus
```






