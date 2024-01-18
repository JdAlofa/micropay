package dtu.dtupay;

import dtu.dtupay.common.RabbitMQ;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import dtu.dtupay.common.Event;

public class DTUPayService {
	private List<String> customers = new ArrayList<>();
	private List<String> merchants = new ArrayList<>();
	private BankService bankService = new BankServiceService().getBankServicePort();
	private RabbitMQ rabbitMQ;

	public DTUPayService() throws Exception {
		rabbitMQ = new RabbitMQ();
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

	public String sayHello(String msg) throws Exception {
		Event event = new Event();
		event.setType("type1");
		event.setPayload("payload1");

		rabbitMQ.sendMessage(event);
		return "hello " + msg;
	}
}
