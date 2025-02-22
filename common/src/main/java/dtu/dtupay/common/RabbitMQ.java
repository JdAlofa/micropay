
package dtu.dtupay.common;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rabbitmq.client.*;

public class RabbitMQ implements AutoCloseable {

	private final static String EXCHANGE_NAME = "hello";
	private final Channel channel;
	private final Connection connection;
	private String queueName;

	public RabbitMQ() throws Exception {
		String host = "localhost";
		String docker_host = System.getenv("RABBITMQ_HOST");
		if (host != null) {
			host = docker_host;
		}
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(5672);
		factory.setUsername("joe");
		factory.setPassword("mama");
		factory.setVirtualHost("/");

		this.connection = factory.newConnection();
		this.channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");
	}

	public void setEventCallback(DeliverCallback callback) throws IOException {
		channel.basicConsume(queueName, true, callback, consumerTag -> {
		});
	}

	public void declareQueue(String queueName) throws IOException {
		this.queueName = queueName;
		channel.queueDeclare(queueName, false, false, false, null);
		channel.queueBind(queueName, EXCHANGE_NAME, "");
	}

	public void sendMessage(Object message) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String jsonMessage = mapper.writeValueAsString(message);
		channel.basicPublish(EXCHANGE_NAME, "", null, jsonMessage.getBytes());
	}

	@Override
	public void close() {
		try {
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
