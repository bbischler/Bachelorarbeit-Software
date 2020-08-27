package de.profi.tesla;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

	@GetMapping("/car")
	public String greeting() {
		return "Hello car!!";
	}
}