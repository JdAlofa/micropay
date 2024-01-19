package dtu.dtupay;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget; // Import the WebTarget class

public class DTUPay {
	WebTarget baseUrl;

	public DTUPay() {
		Client client = ClientBuilder.newClient();
		baseUrl = client.target("http://localhost:8080/");
	}

}
