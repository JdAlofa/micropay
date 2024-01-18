package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import dtu.dtupay.common.Event;
import dtu.dtupay.common.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class DTUPayService {
	private List<String> customers = new ArrayList<>();
	private List<String> merchants = new ArrayList<>();
	private BankService bankService = new BankServiceService().getBankServicePort();
	private RabbitMQ rabbitMQ;
	private Map<UUID, CompletableFuture<String>> pendingResults = new HashMap<>();

	public DTUPayService() throws Exception {
		rabbitMQ = new RabbitMQ();

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");

			ObjectMapper mapper = new ObjectMapper();
			Event event = mapper.readValue(message, Event.class);
			String eventType = event.getType();
			switch (eventType) {
				case "type1":
					CompletableFuture<String> future = pendingResults.get(event.getUUID());
					Token token = mapper.readValue(event.getPayload(), Token.class);
					System.out.println("Received token: " + token);
					future.complete("hello rabbit");
					break;

				default:
					System.out.println("Don't care about this type of event");
					break;
			}
			// rabbitMQ.sendMessage(message);
		};
		rabbitMQ.setEventCallback(deliverCallback);
	}

	public void registerCustomer(String id) {
		customers.add(id);
	}

	public void registerMerchant(String id) {
		merchants.add(id);
	}

	public boolean customerExists(String id) {
		return customers.contains(id);
	}

	public boolean merchantExists(String id) {
		return merchants.contains(id);
	}

	public void deregisterCustomer(String id) {
		customers.remove(id);
	}

	public void deregisterMerchant(String id) {
		customers.remove(id);
	}

	public void transferMoney(String customerId, String merchantId, BigDecimal amount)
			throws BankServiceException_Exception {
		String description = "Payment from " + customerId + " to " + merchantId;
		bankService.transferMoneyFromTo(customerId, merchantId, amount, description);

	}

	public CompletableFuture<String> sayHello(String msg) throws Exception {
		CompletableFuture<String> futureResult = new CompletableFuture<>();
		UUID id = UUID.randomUUID();
		pendingResults.put(id, futureResult);

		Token token = new Token();
		token.setId("234");
		ObjectMapper mapper = new ObjectMapper();
		String jsonMessage = mapper.writeValueAsString(token);
		Event event = new Event();
		event.setUUID(id);
		event.setType("type1");
		event.setPayload(jsonMessage);

		rabbitMQ.sendMessage(event);

		return futureResult;
	}
}
