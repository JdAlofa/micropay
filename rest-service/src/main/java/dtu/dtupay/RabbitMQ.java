package dtu.dtupay;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class RabbitMQ {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		// Connection settings
		String host = "localhost";
		int port = 5672;
		String username = "your_username";
		String password = "your_password";
		String virtualHost = "/";

		// Create a connection factory
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);

		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {

			// Declare a queue
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// Message to send
			String message = "Hello, RabbitMQ!";

			// Publish the message to the queue
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
	}
}
