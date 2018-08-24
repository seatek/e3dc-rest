package seatek.e3dc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAccess {
	/**
	 * Injected property. May come from Command Line Arguments,
	 * System.getProperties(), application.properties, ...
	 */
	@Value("${greeting}")
	private String greeting;

	@Autowired
	private ModbusRepository modbusRepository;


	@RequestMapping("/greeting")
	public String greet() {
		return this.greeting;
	}

	@RequestMapping("/reading/hausleistung")
	public int getHausleistung() {
		return modbusRepository.getHausleistung();
	}

	@RequestMapping("/reading/solarleistung")
	public int getSolarleistung() {
		return modbusRepository.getSolarleistung();
	}

	@RequestMapping("/reading/netzleistung")
	public int getNetzleistung() {
		return modbusRepository.getNetzleistung();
	}

	@RequestMapping("/")
	public Measurement getMeasurement() {
		return modbusRepository.getMeasurement();
	}
}
