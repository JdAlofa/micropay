package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.dtupay.common.Token;

import com.rabbitmq.client.DeliverCallback;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenService {

	public static void main(String[] args) throws Exception {
		try (RabbitMQ rabbitMQ = new RabbitMQ()) {
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				// Process the received message as needed
				ObjectMapper mapper = new ObjectMapper();
				Token token = mapper.readValue(message, Token.class);
				System.out.println("Received: " + token);
				// rabbitMQ.sendMessage(message);
			};
			rabbitMQ.setEventCallback(deliverCallback);
			System.out.println("listening");
			System.in.read();
		} catch (Exception e) {
			throw e;
		}
	}
}
