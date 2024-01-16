package dtu.dtupay;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget; // Import the WebTarget class
import jakarta.ws.rs.core.Response;

public class DTUPay {
	WebTarget baseUrl;

	public DTUPay() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

	public String sayHelloToRabbit(String msg) throws Exception {
		Response response = baseUrl.path("/hellorabbit/{msg}")
				.resolveTemplate("msg", msg)
				.request()
				.post(null);
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new Exception("Server responded with: " + response.getStatus());
		} else {
			String responseBody = response.readEntity(String.class);
			return "hello " + responseBody;
		}
	}
}
