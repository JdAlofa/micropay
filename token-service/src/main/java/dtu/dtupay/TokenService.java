package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.dtupay.common.Event;
import dtu.dtupay.common.Token;

import com.rabbitmq.client.DeliverCallback;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TokenService {

	private static Map<String, Token> tokenDB = new HashMap<>();

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				ObjectMapper mapper = new ObjectMapper();
				Event event = mapper.readValue(message, Event.class);
				String eventType = event.getType().trim();

				System.out.println("Payment Service Received event: " + eventType);
				switch (eventType) {

					case "PaymentRequested":
						System.out.println("  -> handling event: " + eventType);
						String customerId = event.getPayload();
						Event nextEvent = generateTokens(event.getUUID(), customerId);
						try {
							rabbitMQ.sendMessage(nextEvent);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					default:
						System.out.println("  -> ignoring event: " + eventType);
						break;
				}
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

	private static Event generateTokens(UUID eventUUID, String customerId) throws JsonProcessingException {
		// Customer not in token db -> first time asking for tokens
		if (!tokenDB.containsKey(customerId)) {
			List<Token> newTokens = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				newTokens.add(new Token());
			}
			Event event = new Event();
			event.setUUID(eventUUID);
			event.setType("TokensGenerated");
			event.setPayload(newTokens);
			return event;
		}
		Event event = new Event();
		event.setUUID(eventUUID);
		event.setType("TokenGenerationDenied");
		event.setPayload("Not allowed more tokens");
		return event;
	}
}
