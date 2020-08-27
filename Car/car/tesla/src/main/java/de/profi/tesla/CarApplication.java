package de.profi.tesla;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.profi.tesla.thread.GenerateValues;
import de.profi.tesla.thread.TransmitValues;

@SpringBootApplication
public class CarApplication {
	private static Logger logger = LogManager.getLogger();
	private static ConfigurableApplicationContext ctx;
	private static TransmitValues transmitValues;
	private static GenerateValues generateValues;

	public static void main(String[] args) {
		ctx = SpringApplication.run(CarApplication.class, args);
		logger.info("Car is running....");
		init();
	}

	private static void init() {
		// sleep for a random time up to five seconds
		// this jitter avoids overload at simultaneous startup of many services
		try {
			long sleepTime = (long) (Math.random() * 5_000);
			logger.info("Sleeping for: " + sleepTime);
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			logger.error("Faild at startup sleep-jitter.");
			e.printStackTrace();
		}

		logger.info("Starting threads now..");
		generateValues = new GenerateValues();
		generateValues.start();
		transmitValues = new TransmitValues();
		transmitValues.start();

	}

	@PreDestroy
	private static void shutdown() {
		generateValues.stopGeneratingValues();
		transmitValues.stopTransmittingValues();
	}

}
