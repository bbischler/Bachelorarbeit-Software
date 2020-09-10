package de.profi.tesla.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import de.profi.tesla.MessageQueue;
import de.profi.tesla.service.TransmissionService;

public class TransmitValues extends Thread {

	private static Logger logger = LogManager.getLogger();
	private boolean threadRunning = true;
	private MessageQueue queue;
	private TransmissionService transmissionService;

	/**
	 * Constructor for TransmitValues. Gets a TransmissionService, MessageQueue
	 * 
	 */
	public TransmitValues() {
		this.queue = MessageQueue.getInstance();
		this.transmissionService = TransmissionService.getInstance();
	}

	/**
	 * Starts the thread. Puts first MessageQueue entry into the tranmissionService
	 * and deletes that first entryin the queue. Every 3 seconds
	 * 
	 */
	@Override
	public void run() {
		while (threadRunning) {
			int sendingErrorCount = 0;
			while (queue.getQueueSize() > 0) {
				logger.info("Values: " + queue.getFirstQueueEntry());
					// send first entry from the queue
					HttpStatus responseCode = transmissionService.sendCarSensorValues(queue.getFirstQueueEntry());

					if (responseCode.is2xxSuccessful()) {
						queue.deleteFirstQueueEntry(); // delete entry, if sending was successful
						sendingErrorCount = 0; // reset error count
					} else {
						sendingErrorCount++; // increase error count
						if (sendingErrorCount >= 3) {
							logger.error(
									"Three consecutive errors while sending. Aborting and trying again after sleeping.");
							break; // at three consecutive errors, abort sending. Wait for next iteration.
						}
					}
			try {
				Thread.sleep(1_000); // sleep before sending values from queue
			} catch (InterruptedException e) {
				logger.error("Transmitting Values, error while sleeping.");
				e.printStackTrace();
			}
		}
	}
		}

	

	public void stopTransmittingValues() {
		logger.debug("Stop transmitting values at next iteration.");
		threadRunning = false; // stop the tread at the next iteration
	}
}
