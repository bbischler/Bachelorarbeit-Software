package org.quarkus;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Path("/cars")
@CrossOrigin(origins = "*")
public class CarController {

    private static Logger logger = LogManager.getLogger();
    private Integer carCounter = 0;

    @Autowired
    private InfluxDB influxDB;

    public CarController() {
    }

    /**
     * Hello from Carcontroller
     * 
     * @return string
     */
    @GET
    @Produces("application/json")
    public String hello() {
        return "Hello from CarController-Send!!";
    }



    /**
     * Returns the last n SensorValues of the car
     * 
     * @param String carName
     * @param int    number
     * @return QueryResult (List of Sensorvalues)
     */
    @GET
    @Path("/get/{name}/{number}")
    @Produces("application/json")
    public QueryResult getCarValues(@PathParam("name") String carName, @PathParam("number") int number) {
        // spin(1000); // Sleep 1 Second
        logger.info("Getting Data from: " + carName);
        InfluxDBConfiguration.checkDatabaseConnection(influxDB);

        influxDB.setDatabase(InfluxDBConfiguration.CAR_DATABASE_NAME);
        Query query = new Query(String.format("SELECT * FROM %s GROUP BY * ORDER BY DESC LIMIT  %d", carName, number),
                InfluxDBConfiguration.CAR_DATABASE_NAME);
        QueryResult queryResult = influxDB.query(query);

        return queryResult;
    }
}