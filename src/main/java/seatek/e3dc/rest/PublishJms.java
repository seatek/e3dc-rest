package seatek.e3dc.rest;

import java.io.Closeable;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import seatek.e3dc.rest.Measurement.Field;

@Slf4j
@Component
public class PublishJms {
	@Autowired
	Session session;

	private Map<Measurement.Field, Topic> destination;

	/*
	 * (non-Javadoc)
	 *
	 * @see home.weather.activemq.JmsPublisher#start()
	 */
	@PostConstruct
	public void init() throws JMSException {
		this.destination = new EnumMap<Measurement.Field, Topic>(Measurement.Field.class);
		for (Measurement.Field t : Measurement.Field.values()) {
			String topicName = toTopicName(t);
			log.info("Creating topic " + topicName);
			this.destination.put(t, this.session.createTopic(topicName));
		}

	}

	private String toTopicName(Field t) {

		return String.format("home:pv:%s", t.name().toLowerCase(Locale.ENGLISH));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see home.weather.activemq.JmsPublisher#send()
	 */
	public void send(final Measurement.Field type, final int value) throws JMSException {
		// Create a MessageProducer from the Session to the Topic or Queue
		MessageProducer producer = this.session.createProducer(this.destination.get(type));
		producer.setTimeToLive(TimeUnit.MINUTES.toMillis(5));
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// Create a messages
		TextMessage message = this.session.createTextMessage(String.valueOf(value));
		producer.send(message);
	}

}
