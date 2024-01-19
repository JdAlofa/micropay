package dtu.dtupay;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import dtu.dtupay.CustomerClient;
import dtu.dtupay.MerchantClient;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PaymentSteps {

	ArrayList<String> merchants = new ArrayList<String>();
	ArrayList<String> customers = new ArrayList<String>();
	Boolean paymentSuccessful;

	BankService bankService = new BankServiceService().getBankServicePort();
	CustomerClient customer;
	MerchantClient merchant;

	@Given("a customer with a bank account with balance {int}")
	public void a_customer_with_a_bank_account_with_balance(int Balance) throws BankServiceException_Exception {
		String randomString = new Random().ints(24, 'A', 'z' + 1)
				.mapToObj(i -> (char) i)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		User user = new User();
		user.setFirstName("Graig");
		user.setLastName("Davis");
		user.setCprNumber(randomString);
		String account = bankService.createAccountWithBalance(user, BigDecimal.valueOf(400));
		customer = new CustomerClient(
				new Customer(account));
		customers.add(account);
	}

	@Given("that the customer is registered with DTU Pay")
	public void that_the_customer_is_registered_with_DTU_Pay() throws Exception {
		customer.registerAccount();
	}

	@Given("a merchant with a bank account with balance {int}")
	public void a_merchant_with_a_bank_account_with_balance(int Balance) throws BankServiceException_Exception {
		String randomString = new Random().ints(24, 'A', 'z' + 1)
				.mapToObj(i -> (char) i)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		User user = new User();
		user.setFirstName("John");
		user.setLastName("Lewis");
		user.setCprNumber(randomString);
		String account = bankService.createAccountWithBalance(user, BigDecimal.valueOf(400));
		merchant = new MerchantClient(
				new Merchant(account));
		customers.add(account);
	}

	@Given("that the merchant is registered with DTU Pay")
	public void that_the_merchant_is_registered_with_DTU_Pay() throws Exception {
		merchant.registerAccount();
	}

	@When("the merchant initiates a payment for {int} kr by the customer")
	public void the_merchant_initiates_a_payment_for_kr_by_the_customer(int Amount) {
		BigDecimal amount = BigDecimal.valueOf(Amount);
		try {
			merchant.createPayment(customer.getUserId(), amount);
			paymentSuccessful = true;
		} catch (Exception e) {
			paymentSuccessful = false;
		}
	}

	@Then("the payment is successful")
	public void the_payment_is_successful() {
		// Write code here to check that the payment was successful
		assertTrue(paymentSuccessful);
	}

	@Then("the balance of the customer at the bank is {int} kr")
	public void the_balance_of_the_customer_at_the_bank_is_kr(int balance) throws BankServiceException_Exception {
		// Write code here to check the customer's bank balance
		Account account = bankService.getAccount(customers.get(0)); // replace customerId with the actual ID
		BigDecimal postPaymentbalance = account.getBalance();
		assertEquals(balance, postPaymentbalance);

	}

	@Then("the balance of the merchant at the bank is {int} kr")
	public void the_balance_of_the_merchant_at_the_bank_is_kr(int balance) throws BankServiceException_Exception {
		// Write code here to check the merchant's bank balance
		Account account = bankService.getAccount(merchants.get(0)); // replace customerId with the actual ID
		BigDecimal postPaymentbalance = account.getBalance();
		assertEquals(balance, postPaymentbalance);
	}

	@After
	public void delete_accounts() throws BankServiceException_Exception {
		customers.forEach((customer) -> {
			try {
				bankService.retireAccount(customer);
			} catch (BankServiceException_Exception e) {
				// Handle the exception here or rethrow it
				throw new RuntimeException(e); // Rethrow as an unchecked exception
			}
		});
		merchants.forEach((merchant) -> {
			try {
				bankService.retireAccount(merchant);
			} catch (BankServiceException_Exception e) {
				// Handle the exception here or rethrow it
				throw new RuntimeException(e); // Rethrow as an unchecked exception
			}
		});
	}

}
