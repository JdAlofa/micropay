package dtu.dtupay;

import java.util.UUID;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class MerchantClient {

	private Merchant user;
	WebTarget baseUrl;

	public MerchantClient(Merchant user) {
		baseUrl = ClientBuilder.newClient().target("http://localhost:8080/");
		this.user = user;
	}

	public UUID getUserId() {
		return user.getId();
	}

	public String getUserBankAccount() {
		return user.getBankAccount();
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
