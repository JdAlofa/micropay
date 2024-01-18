package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.dtupay.common.Event;

import com.rabbitmq.client.DeliverCallback;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;

public class TokenService {

	public static void main(String[] args) throws Exception {
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				// Process the received message as needed
				ObjectMapper mapper = new ObjectMapper();
				Event token = mapper.readValue(message, Event.class);
				System.out.println("Received: " + token);
				// rabbitMQ.sendMessage(message);
			};
			rabbitMQ.setEventCallback(deliverCallback);
			System.out.println("listening");
			CountDownLatch latch = new CountDownLatch(1);
			latch.await();
		} catch (Exception e) {
			throw e;
		}
	}
}
