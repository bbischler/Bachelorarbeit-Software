package de.profi.tesla.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import de.profi.tesla.bean.SensorValues;

public class TransmissionService {
	private static TransmissionService instance;

	private static Logger logger = LogManager.getLogger();
	// private String url = "http://localhost:8080/cars";
	private String url = "http://pp3-quarkus-recv-pp3.apps.us-east-2.starter.openshift-online.com/cars";

	public TransmissionService() {
	}

	/**
	 * Returns the only instance of TranmissionService
	 * 
	 * @return TransmissionService
	 */
	public static synchronized TransmissionService getInstance() {
		if (TransmissionService.instance == null) {
			TransmissionService.instance = new TransmissionService();
		}
		return TransmissionService.instance;
	}

	/**
	 * Request a car-ID from the backend
	 * 
	 * @return CarId as String
	 */
	public String getCarID() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		RestTemplate restTemplate = new RestTemplate();
		// HttpEntity<SensorValues> request = new HttpEntity<>(headers);
		try {
			Integer id = restTemplate.getForObject(url + "/getid", Integer.class);
			logger.info(id);
			return id.toString();
		} catch (ResourceAccessException e) {
			logger.error("Can't reach Resource. " + e);
		} catch (HttpServerErrorException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error("Unexpected Exception!");
			logger.error(e);
		}
		return "error";

	}

	/**
	 * send car sensor values to the backend. if the backend can't be reached,
	 * "Service-Unavailable" get's returned.
	 * 
	 * @param SensorValues car
	 * @return HttpStatus
	 */
	public HttpStatus sendCarSensorValues(SensorValues car) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<SensorValues> request = new HttpEntity<>(car, headers);
		// return HttpStatus.I_AM_A_TEAPOT;

		try {
			ResponseEntity<?> response = restTemplate.postForEntity(url + "/post", request, null);

			return response.getStatusCode();

		} catch (ResourceAccessException e) {
			logger.error("Can't reach Resource. " + e);
			return HttpStatus.SERVICE_UNAVAILABLE;
		} catch (HttpServerErrorException e) {
			logger.error(e);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			logger.error("Unexpected Exception!");
			logger.error(e);
			return HttpStatus.I_AM_A_TEAPOT;
		}

	}

	/**
	 * Delets the CarID in the Backend
	 * 
	 * @return void
	 */
	public void deleteCar() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		RestTemplate restTemplate = new RestTemplate();
		// HttpEntity<SensorValues> request = new HttpEntity<>(headers);
		try {
			restTemplate.delete(url + "/deleteid", Integer.class);
		} catch (ResourceAccessException e) {
			logger.error("Can't reach Resource. " + e);
		} catch (HttpServerErrorException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error("Unexpected Exception!");
			logger.error(e);
		}
	}
}
