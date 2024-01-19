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

public class MerchantRegistrationSteps {

	BankService bankService = new BankServiceService().getBankServicePort();
	MerchantClient client = new MerchantClient(new Merchant("test"));
	DTUPay dtuPay = new DTUPay();
	Boolean successfulRegistration;

	@Given("the new merchant provides a name and bank account")
	public void the_new_merchant_provides_name_and_bank_account() throws BankServiceException_Exception {
		String randomString = new Random().ints(24, 'A', 'z' + 1)
				.mapToObj(i -> (char) i)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		User user = new User();
		user.setFirstName("Graig");
		user.setLastName("Davis");
		user.setCprNumber(randomString);
		client = new MerchantClient(
				new Merchant(bankService.createAccountWithBalance(user, BigDecimal.valueOf(400))));
	}

	@And("the new merchant is already registered")
	public void the_new_merchant_is_already_registered() throws Exception {
		client.registerAccount();
	}

	@When("the merchant app registers the user")
	public void the_merchant_app_registers_the_user() {
		try {
			client.registerAccount();
			successfulRegistration = true;
		} catch (Exception e) {
			successfulRegistration = false;
		}
	}

	@Then("the merchant is created")
	public void the_merchant_is_created() {
		assertTrue(successfulRegistration);
	}

	@Then("the merchant is not created")
	public void a_new_merchant_is_not_created() {
		assertFalse(successfulRegistration);
	}

	@After("@MerchantRegistration")
	public void delete_accounts() throws Exception {
		client.deregisterAccount();
		bankService.retireAccount(client.getUserBankAccount());
	}

}
