
package dtu.dtupay.common;

import java.io.IOException;

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

	public void sendMessage(String message) throws Exception {
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		System.out.println("Sent: " + message + "'");
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

	public static void main(String[] args) throws Exception {
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {

			rabbitMQ.setEventCallback((consumerTag, delivery) -> {
				String receivedMessage = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + receivedMessage + "'");
			});

			rabbitMQ.sendMessage("hello");
			System.in.read();
		}
	}
}
