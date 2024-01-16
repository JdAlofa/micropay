
package dtu.dtupay;

import java.io.IOException;

import com.rabbitmq.client.*;

public class RabbitMQ implements AutoCloseable {

	private final static String EXCHANGE_NAME = "hello";
	private final Channel channel;
	private final Connection connection;

	public RabbitMQ() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("joe");
		factory.setPassword("mama");
		factory.setVirtualHost("/");

		this.connection = factory.newConnection();
		this.channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(
					String consumerTag,
					Envelope envelope,
					AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String message = new String(body, "UTF-8");
				System.out.println("Consumed: " + message);
			}
		};
		channel.basicConsume(queueName, true, consumer);
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

			rabbitMQ.sendMessage("hello");
			System.in.read();
		}
	}
}
