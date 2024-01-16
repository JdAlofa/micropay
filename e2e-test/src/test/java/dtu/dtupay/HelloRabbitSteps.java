package dtu.dtupay;

import static org.junit.Assert.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class HelloRabbitSteps {

	DTUPay dtuPay = new DTUPay();
	String requestMessage;
	String responseMessage;

	@Given("the string {string}")
	public void the_string(String str) {
		requestMessage = str;
	}

	@When("a post is made to the hellorabbit endpoint on the rest service")
	public void a_post_request_is_made_to_the_hellorabbit_endpoint_on_the_rest_service()
			throws Exception {
		responseMessage = dtuPay.sayHelloToRabbit(requestMessage);
	}

	// @When("a post is made to the hellorabbit endpoint on the rest service")
	// public void
	// a_post_request_is_made_to_the_hellorabbit_endpoint_on_the_rest_service()
	// throws Exception {
	// responseMessage = "hello rabbit";
	// }

	@Then("the string {string} is returned")
	public void the_customer_is_created(String str) {
		assertEquals(str, responseMessage);
	}

}
