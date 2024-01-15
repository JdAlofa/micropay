package dtu.dtupay;

import dtu.dtupay.common.Merchant;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget; // Import the WebTarget class
import jakarta.ws.rs.core.Response;

public class MerchantClient {

	private Merchant user;
	WebTarget baseUrl;

	public MerchantClient(Merchant user) {
		baseUrl = ClientBuilder.newClient().target("http://localhost:8080/");
		this.user = user;
	}

	public String getUserId() {
		return user.getId();
	}

	public void registerAccount() throws Exception {
		Response response = baseUrl.path("/customers/{id}")
				.resolveTemplate("id", user.getId())
				.request()
				.post(null);
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new Exception("Server responded with: " + response.getStatus());
		}
	}

	public void deregisterAccount() throws Exception {
		Response response = baseUrl.path("/customers/{id}")
				.resolveTemplate("id", user.getId())
				.request()
				.delete();
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new Exception("Server responded with: " + response.getStatus());
		}
	}
}
