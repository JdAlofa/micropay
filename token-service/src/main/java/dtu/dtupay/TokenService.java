package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.dtupay.common.Event;
import dtu.dtupay.common.Token;

import com.rabbitmq.client.DeliverCallback;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;

public class TokenService {

	public static void main(String[] args) {
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				ObjectMapper mapper = new ObjectMapper();
				Event event = mapper.readValue(message, Event.class);
				String eventType = event.getType();
				System.out.println("Received event: " + eventType);
				switch (eventType) {
					case "type1":
						Token token = mapper.readValue(event.getPayload(), Token.class);
						System.out.println("Received token: " + token);
						break;

					case "TokensRequested":
						System.out.println("Handling event: " + eventType);
						// String customerId = mapper.readValue(event.getPayload(), String.class);
						String customerId = event.getPayload();
						// functions with business logic returning an event
						Event generatedTokenEvent = new Event();
						generatedTokenEvent.setUUID(event.getUUID());
						generatedTokenEvent.setType("TokensGenerated");
						generatedTokenEvent.setPayload("THIS IS A NEW TOKEN");
						try {
							rabbitMQ.sendMessage(generatedTokenEvent);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					default:
						System.out.println("Ignoring event: " + eventType);
						break;
				}
				// rabbitMQ.sendMessage(message);
			};
			rabbitMQ.setEventCallback(deliverCallback);

			System.out.println("listening");

			// Use latch to keep main running
			CountDownLatch latch = new CountDownLatch(1);
			latch.await();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
