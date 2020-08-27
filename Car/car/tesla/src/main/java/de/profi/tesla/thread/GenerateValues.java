package de.profi.tesla.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.profi.tesla.MessageQueue;
import de.profi.tesla.SensorSimulation;
import de.profi.tesla.bean.SensorValues;
import de.profi.tesla.service.TransmissionService;

public class GenerateValues extends Thread {

	private static Logger logger = LogManager.getLogger();
	private boolean threadRunning = true;
	private MessageQueue queue;
	private TransmissionService transmissionService;

	private SensorSimulation sensorSimulation;

	/**
	 * Constructor for GenerateValues. Gets a TransmissionService, MessageQueue and
	 * SensorSimulation
	 * 
	 */
	public GenerateValues() {
		this.transmissionService = TransmissionService.getInstance();
		this.sensorSimulation = new SensorSimulation(this.transmissionService.getCarID());
		this.queue = MessageQueue.getInstance();

	}

	/**
	 * Starts the thread. Gets new SensorValues and adds them to the MessageQueue
	 * 
	 */
	@Override
	public void run() {
		while (threadRunning) {
			SensorValues values = sensorSimulation.newValues();
			logger.debug("New sensor values are: " + values);
			this.queue.addToQueue(values);

			try {
				Thread.sleep(1_000);
			} catch (InterruptedException e) {
				logger.error("Generate Values, error while sleeping.");
				e.printStackTrace();
			}
		}
		logger.info("Thread for value generation says goodbye :)");
	}

	/**
	 * Stops the thread
	 * 
	 * @return void
	 */
	public void stopGeneratingValues() {
		logger.debug("Stop generating values at next iteration.");
		this.transmissionService.deleteCar();
		threadRunning = false; // stop the tread at the next iteration
	}
}
