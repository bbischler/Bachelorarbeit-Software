# PP3 Car-Simulation
Dieses Java-Programm simuliert Sensordaten von einem Auto. Daten werden zufällig, aber in einem festgelegtem Intervall erzeugt und an ein Backend gesendet, welches in einer Openshift-Cloud liegt. 

### Building
Um die App zu starten den Code clonen und folgenden Befehl ausführen:
```sh
$ docker-compose up --build
```
Da das Auto in der einem Docker Image läuft, können mehrere Instanzen gleichzeitig gestartet werden. 
Folgender Befehl startet beispielsweise 5 Autos:
```sh
$ docker-compose up --build --scale car=5
```