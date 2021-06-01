package seatek.e3dc.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JmsProvider {
	@Value("${jms.broker.url}")
	private  String brokerUrl;
	private Connection connection;
	private Session session;


	@PostConstruct
	public void init() throws JMSException {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);

			// Create a Connection
			this.connection = connectionFactory.createConnection();
			this.connection.start();

			// Create a Session
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	@Bean
	public Connection provideConnection() {
		return connection;
	}
	@Bean
	public Session provideSession() {
		return session;
	}

	@PreDestroy
	public void destroy() {
		try {
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			session.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
