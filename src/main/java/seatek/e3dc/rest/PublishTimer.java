package seatek.e3dc.rest;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import seatek.e3dc.rest.Measurement.Field;

@Component
@Slf4j
public class PublishTimer {
	@Autowired
	ModbusRepository repository;
	@Autowired
	private PublishJms jmsPublisher;

	@Scheduled(fixedRate = 10000)
	public void readAndPublish() throws JMSException {
		for(Field field : Field.values()) {
		int value = repository.getValue(field);
		jmsPublisher.send(field, value);
		log.debug("published field "+field+" with value "+value);
	}}
}
