package seatek.e3dc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAccess {

	@Autowired
	private ModbusRepository modbusRepository;

	@RequestMapping("/")
	public Measurement getMeasurement() {
		return this.modbusRepository.getMeasurement();
	}
}
