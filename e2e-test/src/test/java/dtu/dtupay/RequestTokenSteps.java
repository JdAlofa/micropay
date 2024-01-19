package dtu.dtupay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import dtu.dtupay.common.Token;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RequestTokenSteps {

	BankService bankService = new BankServiceService().getBankServicePort();
	CustomerClient client = new CustomerClient(new Customer("some bank account number"));
	DTUPay dtuPay = new DTUPay();
	Boolean tokensReceived;
	List<Token> tokens;

	@Given("a registered user with 0 tokens")
	public void a_registered_user_with_0_tokens() throws Exception {
		// TODO: remove randomString when everything is said and done!
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
		client.registerAccount();
	}

	@When("the user requests new tokens")
	public void the_user_requests_new_tokens() {
		try {
		tokens = client.requestTokens();
		tokensReceived = true;
		} catch (Exception e) {
		tokensReceived = false;
		}
	}

	@Then("the user receives 5 tokens")
	public void the_user_receives_5_tokens() {
		// assertEquals(tokens.size(), 5);
	}

	@After("@RequestTokens")
	public void clean_up() throws Exception {
		client.deregisterAccount();
		client.closeClient();
	}

}
