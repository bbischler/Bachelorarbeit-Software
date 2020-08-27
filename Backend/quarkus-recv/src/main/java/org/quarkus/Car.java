package org.quarkus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

	private String name;
	private long time;
	private double oilLevel;
	private double totalMileage;
	private double gasLevel;
	private double airPressureFrontLeft;
	private double airPressureFrontRight;
	private double airPressureRearLeft;
	private double airPressureRearRight;

	/**
	 * Overrides the toString Method
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return ("Name: " + this.name + ", Oil: " + this.oilLevel + ", Time: " + this.time);
	}

}