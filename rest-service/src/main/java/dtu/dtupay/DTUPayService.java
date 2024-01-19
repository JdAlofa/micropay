package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.dtupay.common.TokenGenerationPayload;
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
			CompletableFuture<String> future;
			System.out.println("Received event: " + eventType);
			switch (eventType) {
				// case "type1":
				// future = pendingResults.get(event.getUUID());
				// Token token_test = mapper.readValue(event.getPayload(), Token.class);
				// future.complete("hello rabbit");
				// break;

				case "TokensGenerated":
					System.out.println("  -> Handling event: " + eventType);
					future = pendingResults.get(event.getUUID());
					String tokens = event.getPayload();
					future.complete(tokens);
					break;

				default:
					System.out.println("  -> Ignoring event: " + eventType);
					break;
			}
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
		merchants.remove(id);
	}

	public void transferMoney(String customerId, String merchantId, BigDecimal amount)
			throws BankServiceException_Exception {
		String description = "Payment from " + customerId + " to " + merchantId;
		bankService.transferMoneyFromTo(customerId, merchantId, amount, description);

	}

	public CompletableFuture<String> requestTokens(String id) throws Exception {
		CompletableFuture<String> futureResult = new CompletableFuture<>();
		UUID eventUUID = UUID.randomUUID();
		Event event = new Event();
		event.setUUID(eventUUID);
		event.setType("TokensRequested");
		event.setPayload(id);

		pendingResults.put(eventUUID, futureResult);
		rabbitMQ.sendMessage(event);

		return futureResult;
	}
}
