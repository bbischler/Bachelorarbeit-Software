package org.quarkus;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDBIOException;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

@Configuration
public class InfluxDBConfiguration {
	public InfluxDBConfiguration() {
	}

	private static Logger logger = LogManager.getLogger();

	private static final String RETENTION_POLICY_NAME = "oneMonthOneReplica";
	private static final String DURATION_OF_DATA = "30d";
	private static final int DATA_REPLICATION = 1;
	public static String CAR_DATABASE_NAME = "car";
	private static InfluxDB influxDB;
	private static String influxDBUrl = "http://influxdb:8086/";

	public InfluxDBConfiguration(@Value("${INFLUXDB_URL}") String influxUrl) {
		influxDBUrl = influxUrl;
		logger.info("URL to InfluxDB was set. It is now: " + influxDBUrl);
	}

	/**
	 * Checks connection to InfluxDB
	 * 
	 * @return InfluxDB
	 */
	@Bean
	public static InfluxDB influxDBConnection() throws InterruptedException {

		logger.info("Connecting to database. URL: " + influxDBUrl);

		int pingErrorCounter = 0;
		while (pingErrorCounter < 6) { // try database connection for maximal 30 seconds ( 30s = 6 * 5s )

			try {
				influxDB = InfluxDBFactory.connect(influxDBUrl);
				Pong ping = influxDB.ping(); // try to ping database-server

				if (ping.getVersion().equalsIgnoreCase("unknown")) { // error, something's wrong with the database
					throw new InfluxDBIOException(new IOException("Version number of Database is unknown"));
				}

				// success
				logger.info("Connection to database was successful");
				break; // continue with configuration

			} catch (InfluxDBIOException e) {
				logger.error("Error reaching database-server! " + e);
				pingErrorCounter++; // increase error counter
				if (pingErrorCounter >= 6) { // maximum of 30 seconds ( 30 = 5 * 6 )
					return null; // service will fail to start
				} else {
					Thread.sleep(5_000); // wait for five seconds, then try again to ping the server
				}
			}
		}

		// create all necessary databases, if they don't already exist
		influxDB.query(new Query("CREATE DATABASE " + CAR_DATABASE_NAME, CAR_DATABASE_NAME)); // car database

		// create retention policies for all three databases
		String createRetentionPolicyQuery = "CREATE RETENTION POLICY %s ON %s DURATION %s REPLICATION %d DEFAULT";
		influxDB.query(new Query(String.format(createRetentionPolicyQuery, RETENTION_POLICY_NAME, CAR_DATABASE_NAME,
				DURATION_OF_DATA, DATA_REPLICATION), CAR_DATABASE_NAME)); // car retention policy

		influxDB.setRetentionPolicy(RETENTION_POLICY_NAME);

		// write to database every 200 elements or at least 100 ms. 500 ms jitter to
		// avoid overload
		influxDB.enableBatch(BatchOptions.DEFAULTS.actions(200).flushDuration(100).jitterDuration(500));

		influxDB.setLogLevel(InfluxDB.LogLevel.FULL); // The available levels are BASIC, FULL, HEADERS, and NONE.

		return influxDB;
	}

	/**
	 * Deletes database
	 */
	public static void dropDatabases() {
		// drop database
		influxDB.query(new Query("DROP DATABASE " + CAR_DATABASE_NAME, CAR_DATABASE_NAME)); // delete car database
	}

	/**
	 * Tries to ping the database. Throws HttpServerErrorException, if the
	 * database-server is not reachable or the version is unknown.
	 * 
	 * @param influxDB
	 */
	public static void checkDatabaseConnection(InfluxDB influxDB) {
		try {
			Pong ping = influxDB.ping(); // check connection to database
			if (ping.getVersion().equalsIgnoreCase("unknown")) {
				logger.error("Error pinging database-server!");
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error pinging database. Unknown Version number."); // throw error if database is not reachable
			}
		} catch (InfluxDBIOException e) {
			logger.error("Error reaching database-server! " + e);
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Influx Database is not reachable.");
		}
	}

	@PreDestroy
	private void onShutdown() {
		logger.info("Closing database connection..");

		influxDB.disableBatch();
		influxDB.close();

		logger.info("Endpoint says goodbye :)");
	}

}
