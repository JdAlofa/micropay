package dtu.dtupay;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Random;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

public class CustomerRegistrationSteps {

	BankService bankService = new BankServiceService().getBankServicePort();
	CustomerClient client;
	DTUPay dtuPay = new DTUPay();
	Boolean successfulRegistration;

	@Given("the user provides a name and bank account")
	public void the_user_provides_name_and_bank_account() throws BankServiceException_Exception {
		String randomString = new Random().ints(24, 'A', 'z' + 1)
				.mapToObj(i -> (char) i)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		User user = new User();
		user.setFirstName("Graig");
		user.setLastName("Davis");
		user.setCprNumber(randomString);
		client = new CustomerClient(
				new Customer(bankService.createAccountWithBalance(user, BigDecimal.valueOf(400))));
	}

	@And("the user is already registered")
	public void the_user_is_already_registered() throws Exception {
		client.registerAccount();
	}

	@When("the customer app registers the user")
	public void the_customer_app_registers_the_user() {
		try {
			client.registerAccount();
			successfulRegistration = true;
		} catch (Exception e) {
			successfulRegistration = false;
		}
	}

	@Then("the customer is created")
	public void the_customer_is_created() {
		assertTrue(successfulRegistration);
	}

	@Then("the customer is not created")
	public void a_new_customer_is_not_created() {
		assertFalse(successfulRegistration);
	}

	@After("@CustomerRegistration")
	public void delete_accounts() throws Exception {
		client.deregisterAccount();
		bankService.retireAccount(client.getUserBankAccount());
	}

}
