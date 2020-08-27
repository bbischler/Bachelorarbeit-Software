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
        return "Hello from CarController-Recv!!";
    }

    /**
     * Return carID
     * 
     * @return int carID
     */
    @GET
    @Path("/getid")
    @Produces("application/json")
    public Integer getCarID() {
        return carCounter++;
    }

    /**
     * Deletes carID
     * 
     * @return int carID
     */
    @DELETE
    @Path("/deleteid")
    @Produces("application/json")
    public Integer deleteID() {
        return carCounter--;
    }

    /**
     * Returns database-name
     * 
     * @return String
     */
    @GET
    @Path("/db")
    @Produces("application/json")
    public String dbName() {
        return InfluxDBConfiguration.CAR_DATABASE_NAME;
    }

    /**
     * Adds the Sensorvalues of a car to the database
     * 
     * @param Car car
     * @return car
     */
    @POST
    @Path("/post")
    @Consumes("application/json")
    @Produces("application/json")
    public Car postCar(Car car) {
        // spin(500); // Sleep 0.5 Second
        InfluxDBConfiguration.checkDatabaseConnection(influxDB);

        logger.info("Adding sensor values for car: " + car.getName());

        Point point = Point.measurement("tesla" + car.getName()).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("oilLevel", car.getOilLevel()).addField("time", car.getTime())
                .addField("totalMileage", car.getTotalMileage()).addField("gasLevel", car.getGasLevel())
                .addField("airPressureFrontLeft", car.getAirPressureFrontLeft())
                .addField("airPressureFrontRight", car.getAirPressureFrontRight())
                .addField("airPressureRearLeft", car.getAirPressureRearLeft())
                .addField("airPressureRearRight", car.getAirPressureRearRight()).build();

        influxDB.setDatabase(InfluxDBConfiguration.CAR_DATABASE_NAME);
        influxDB.write(point);
        logger.debug("Point was written for car: " + car.getName());
        return car;
    }

    /**
     * Deletes all data
     * 
     * @return String
     */
    @GET
    @Path("/flush")
    @Produces("application/json")
    public String deleteDatabase() {
        logger.info("Now removing all data from database..");
        try {
            // check connection
            InfluxDBConfiguration.checkDatabaseConnection(influxDB);
            // delete databases
            InfluxDBConfiguration.dropDatabases();
            // recreate databases
            InfluxDBConfiguration.influxDBConnection();

        } catch (ResourceAccessException e) {
            logger.error("Can't reach Resource. " + e);
            return HttpStatus.SERVICE_UNAVAILABLE.toString();
        } catch (HttpServerErrorException e) {
            logger.error(e);
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        } catch (InterruptedException e) {
            logger.error("Error while connecting to database: " + e);
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        } catch (Exception e) {
            logger.error("Unexpected Exception!");
            logger.error(e);
            return HttpStatus.I_AM_A_TEAPOT.toString();
        }
        return "All data has been removed.";
    }
}