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
import java.util.ArrayList;

public class TokenService {

	private static Map<String, ArrayList<Token>> tokenDB = new HashMap<>();

	public static void main(String[] args) {
		try {
			// Sleep for 10 seconds
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// Handle exception
			e.printStackTrace();
		}
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				ObjectMapper mapper = new ObjectMapper();
				Event event = mapper.readValue(message, Event.class);
				String eventType = event.getType().trim();
				System.out.println(" Token Service Received event: " + eventType);
				switch (eventType) {

					case "TokensRequested":
						System.out.println("  -> Token Service is handling event: " + eventType);
						String customerId = event.getPayload();
						int numberOfTokensRequested = event.getExtra();
						Event nextEvent = generateTokens(event.getUUID(), customerId, numberOfTokensRequested);
						try {
							rabbitMQ.sendMessage(nextEvent);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "TokenVerificationRequested":
						System.out.println("  -> Token Service is handling event: " + eventType);
						String tokenID = event.getPayload();
						Event Event2 = verifyUsedToken(event.getUUID(), tokenID);
						try {
							rabbitMQ.sendMessage(Event2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
						case "TokenInvalidationRequested":
						System.out.println("  -> Token Service is handling event: " + eventType);
						String tokenId = event.getPayload();
						Event Event3 = setUsedToken(event.getUUID(), tokenId);
						try {
							rabbitMQ.sendMessage(Event3);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					default:
						System.out.println("  -> Token Service is ignoring event: " + eventType);
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


	private static Event verifyUsedToken (UUID eventUUID, String tokenID) throws JsonProcessingException {
		// Iterate over all customers in the tokenDB
		for (Map.Entry<String, ArrayList<Token>> entry : tokenDB.entrySet()) {
			ArrayList<Token> existingTokens = entry.getValue();
	
			Token token = existingTokens.get(0);
	
				// Check if the token ID matches the one passed as parameter
				if (token.getId().equals(tokenID)) {
					// If the token is valid
					if (token.getValid()) {
										
	
						// Return a new event indicating that the token is valid
						Event event = new Event();
						event.setUUID(eventUUID);
						event.setType("TokenValid");
						return event;
					} else {
						// Return a new event indicating that the token is invalid
						Event event = new Event();
						event.setUUID(eventUUID);
						event.setType("TokenInvalid");
						return event;
					}
				}
			
		}	
		// If the token ID does not match any existing token
		Event event = new Event();
		event.setUUID(eventUUID);
		event.setType("TokenInvalid");
		return event;
	}

	private static Event setUsedToken (UUID eventUUID, String tokenID) throws JsonProcessingException {
		// Iterate over all customers in the tokenDB
		for (Map.Entry<String, ArrayList<Token>> entry : tokenDB.entrySet()) {
			ArrayList<Token> existingTokens = entry.getValue();
	
			Token token = existingTokens.get(0);				
						token.setValid(false);
	
						// Return a new event indicating that the token is invalid
						Event event = new Event();
						event.setUUID(eventUUID);
						event.setType("TokenInvalidated");
						return event;
					} 
			
			
		// If the token ID does not match any existing token
		Event event = new Event();
		event.setUUID(eventUUID);
		event.setType("TokenInvalid");
		return event;
	}


	private static Event generateTokens(UUID eventUUID, String customerId, int numberOfTokensRequested)
			throws JsonProcessingException {
		// Customer not in token db -> first time asking for tokens
		if (!tokenDB.containsKey(customerId)) {
			ArrayList<Token> newTokens = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				newTokens.add(new Token());
			}
			tokenDB.put(customerId, (ArrayList<Token>) newTokens);
			Event event = new Event();
			event.setUUID(eventUUID);
			event.setType("TokensGenerated");
			event.setPayload(newTokens);
			return event;
		} else if (tokenDB.containsKey(customerId)) {
			ArrayList<Token> existingTokens = tokenDB.get(customerId);
			if (existingTokens.size() <= 1) {
				// when the size is less than or equal to 1
				ArrayList<Token> newTokens = new ArrayList<>();
				if (numberOfTokensRequested > 5) { // if more than 5 tokens are requested
					Event event = new Event();
					event.setUUID(eventUUID);
					event.setType("TokenGenerationDenied");
					event.setPayload("Not allowed to request more than 5 tokens");
					return event;
				} else if (numberOfTokensRequested > 0 && numberOfTokensRequested <= 5) { // if an appropriate number of																							// tokens are requested
					for (int i = 0; i < numberOfTokensRequested; i++) {
						newTokens.add(new Token());
					}
					tokenDB.put(customerId, (ArrayList<Token>) newTokens);
					Event event = new Event();
					event.setUUID(eventUUID);
					event.setType("TokensGenerated");
					event.setPayload(newTokens);
					return event;
				}

			} else if (existingTokens.size() > 1) {
				// when the size is greater than 1
				Event event = new Event();
				event.setUUID(eventUUID);
				event.setType("TokenGenerationDenied");
				event.setPayload("You still have usable tokens");
				return event;
			}
		}

		Event event = new Event();
		event.setUUID(eventUUID);
		event.setType("TokenGenerationDenied");
		event.setPayload("Not allowed more tokens");
		return event;

	}

}
