package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import com.rabbitmq.client.DeliverCallback;

public class TokenService {

	public static void main(String[] args) throws Exception {
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				// Process the received message as needed
			};
			rabbitMQ.setEventCallback(deliverCallback);
			System.out.println("listening");
			System.in.read();
		} catch (Exception e) {
			throw e;
		}
	}
}
