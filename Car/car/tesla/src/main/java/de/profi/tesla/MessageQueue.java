package de.profi.tesla;

import java.util.ArrayList;
import java.util.List;

import de.profi.tesla.bean.SensorValues;

public class MessageQueue {

	private static volatile MessageQueue instance = new MessageQueue();

	private List<SensorValues> queue;

	/**
	 * @return the only instance of the Message Queue
	 */
	public static MessageQueue getInstance() {
		return instance;
	}

	/**
	 * Add Sensorvalues to the Queue
	 * 
	 * @param SencorValues s
	 * @return void
	 */
	public synchronized void addToQueue(SensorValues s) {
		this.queue.add(s);
	}

	/**
	 * Get the queues size
	 * 
	 * @return int
	 */
	public int getQueueSize() {
		return this.queue.size();
	}

	/**
	 * Returns the first Queue entry
	 * 
	 * @return SensorValue
	 */
	public SensorValues getFirstQueueEntry() {
		return this.queue.get(0);
	}

	/**
	 * Deletes the first queue entry
	 * 
	 * @return void
	 */
	public synchronized void deleteFirstQueueEntry() {
		this.queue.remove(0);
	}

	/**
	 * private, because nobody shall call this constructor
	 */
	private MessageQueue() {
		this.queue = new ArrayList<SensorValues>();
	}
}
