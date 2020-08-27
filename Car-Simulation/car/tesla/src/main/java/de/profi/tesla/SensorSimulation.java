package de.profi.tesla;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import de.profi.tesla.bean.SensorValues;

public class SensorSimulation {
	private static final DecimalFormatSymbols DECIMAL_SYMBOL = DecimalFormatSymbols.getInstance();
	SensorValues old;
	String name = "";

	private static final double MAX_OIL_LEVEL = 100.0;
	private static final double MIN_OIL_LEVEL = 1.0;
	private static final double MAX_TOTAL_MILEAGE = 100.0;
	private static final double MIN_TOTAL_MILEAGE = 0.0;
	private static final double MAX_GAS_LEVEL = 100.0;
	private static final double MIN_GAS_LEVEL = 10.0;
	private static final double MAX_AIR_PRESSURE = 3.0;
	private static final double MIN_AIR_PRESSURE = 1.0;

	/**
	 * Constructor for SensorSimulation. Creates the first randomized Sensorvalues
	 * 
	 * @param String carId
	 */
	public SensorSimulation(String carId) {
		name = carId;
		DECIMAL_SYMBOL.setDecimalSeparator('.');
		old = completelyRandomSensorValues();
	}

	/**
	 * Generates new SensorValues, based on the old ones
	 * 
	 * @return SensorValues
	 */
	public SensorValues newValues() {
		SensorValues newValues = new SensorValues();
		newValues.setName(name);
		newValues.setTime(System.currentTimeMillis());
		newValues.setOilLevel(newOilLevel(old.getOilLevel()));
		newValues.setGasLevel(newGasLevel(old.getGasLevel()));
		newValues.setTotalMileage(newTotalMileageLevel(old.getTotalMileage()));
		newValues.setAirPressureFrontLeft(newAirPressure(old.getAirPressureFrontLeft())); // Front left
		newValues.setAirPressureFrontRight(newAirPressure(old.getAirPressureFrontRight())); // Front right
		newValues.setAirPressureRearLeft(newAirPressure(old.getAirPressureRearLeft())); // Rear left
		newValues.setAirPressureRearRight(newAirPressure(old.getAirPressureRearRight())); // rear right
		old = newValues;
		return newValues;
	}

	/**
	 * Generates random SensorValues based on MIN and MAX boundaries
	 * 
	 * @return SensorValues
	 */
	public SensorValues completelyRandomSensorValues() {
		SensorValues s = new SensorValues();
		s.setName(name);
		s.setTime(System.currentTimeMillis());
		s.setOilLevel(randomInBounds(MIN_OIL_LEVEL, MAX_OIL_LEVEL));
		s.setTotalMileage(randomInBounds(MIN_TOTAL_MILEAGE, MAX_TOTAL_MILEAGE));
		s.setGasLevel(randomInBounds(MIN_GAS_LEVEL, MAX_GAS_LEVEL));
		s.setAirPressureFrontLeft(randomInBounds(MIN_AIR_PRESSURE, MAX_AIR_PRESSURE));
		s.setAirPressureFrontRight(randomInBounds(MIN_AIR_PRESSURE, MAX_AIR_PRESSURE));
		s.setAirPressureRearLeft(randomInBounds(MIN_AIR_PRESSURE, MAX_AIR_PRESSURE));
		s.setAirPressureRearRight(randomInBounds(MIN_AIR_PRESSURE, MAX_AIR_PRESSURE));
		return s;
	}

	/**
	 * Generates new OilLevel based in the old level
	 * 
	 * @param double oldOilLevel
	 * 
	 * @return double
	 */
	private double newOilLevel(double oldOilLevel) {
		if (oldOilLevel < MIN_OIL_LEVEL) { // if the oil is below the minimum, it gets completely filled up
			return twoDecimalDigits(MAX_OIL_LEVEL);
		}
		return twoDecimalDigits(oldOilLevel - randomInBounds(0.0, 0.2));
	}

	/**
	 * Generates new TotalMileage based in the old mileage
	 * 
	 * @param double oldTotalMileage
	 * 
	 * @return double
	 */
	private double newTotalMileageLevel(double oldTotalMileage) {
		if (oldTotalMileage > MAX_TOTAL_MILEAGE * 10) { // if the mileage is greater than 1000, it gets reset
			return twoDecimalDigits(MIN_TOTAL_MILEAGE);
		}
		return twoDecimalDigits(oldTotalMileage + randomInBounds(0.0, 2.2));
	}

	/**
	 * Generates new GasLevel based in the old level
	 * 
	 * @param double oldGasLevel
	 * 
	 * @return double
	 */
	private double newGasLevel(double oldGasLevel) {
		if (oldGasLevel < MIN_GAS_LEVEL) { // if the gas is below the minimum, it gets completely filled up
			return twoDecimalDigits(MAX_GAS_LEVEL);
		}
		return twoDecimalDigits(oldGasLevel - randomInBounds(0.08, 0.2));
	}

	/**
	 * Generates new Airpressure based in the old level
	 * 
	 * @param double oldAirPressure
	 * 
	 * @return double
	 */
	private double newAirPressure(double oldAirPressure) {
		if (oldAirPressure < MIN_AIR_PRESSURE) { // if the tire is empty, it gets completely filled up
			return twoDecimalDigits(MAX_AIR_PRESSURE);
		}
		return twoDecimalDigits(oldAirPressure - randomInBounds(0.0, 0.2));
	}

	/**
	 * Generates random Value between lowerBond and upperBond
	 * 
	 * @param double lowerBound * @param double upperBound
	 * 
	 * @return double
	 */
	private double randomInBounds(double lowerBound, double upperBound) {
		double range = upperBound - lowerBound;
		return Math.random() * range + lowerBound;
	}

	/**
	 * Formats Double Value into twoDecimalDigits
	 * 
	 * @param double value
	 * 
	 * @return double
	 */
	private static double twoDecimalDigits(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setDecimalFormatSymbols(DECIMAL_SYMBOL);
		return Double.parseDouble(df.format(value));
	}

}
