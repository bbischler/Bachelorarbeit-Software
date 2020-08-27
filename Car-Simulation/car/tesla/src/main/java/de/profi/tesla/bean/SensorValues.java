package de.profi.tesla.bean;

import lombok.Data;

@Data
public class SensorValues {

	private String name = "";
	private long time;
	private double oilLevel;
	private double totalMileage;
	private double gasLevel;
	private double airPressureFrontLeft;
	private double airPressureFrontRight;
	private double airPressureRearLeft;
	private double airPressureRearRight;

	public SensorValues() {
	}
}
