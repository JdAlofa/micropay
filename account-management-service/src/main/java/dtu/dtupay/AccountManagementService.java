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

public class AccountManagementService {

	private static Map<String, Token> tokenDB = new HashMap<>();

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				ObjectMapper mapper = new ObjectMapper();
				Event event = mapper.readValue(message, Event.class);
				String eventType = event.getType().trim();

				System.out.println("Account Management Service Received event: " + eventType);
				switch (eventType) {

					// case "PaymentRequested":
					// 	System.out.println("  -> Handling event: " + eventType);
					// 	String rawPayment = event.getPayload();
					// 	Event nextEvent = validateToken(event.getUUID(), rawPayment);
					// 	try {
					// 		rabbitMQ.sendMessage(nextEvent);
					// 	} catch (Exception e) {
					// 		e.printStackTrace();
					// 	}
					// 	break;

					default:
						System.out.println("  -> Ignoring event: " + eventType);
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


}
