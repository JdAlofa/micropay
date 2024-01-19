package dtu.dtupay;

import java.math.BigDecimal;
import java.util.UUID;

import dtu.dtupay.common.PaymentRequestPayload;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class MerchantClient {

	private Merchant user;
	private Client client;
	WebTarget baseUrl;

	public MerchantClient(Merchant user) {
		client = ClientBuilder.newClient();
		baseUrl = ClientBuilder.newClient().target("http://localhost:8080/");
		this.user = user;
	}

	public void closeClient() {
		this.client.close();
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

	public void createPayment(UUID tokenId, BigDecimal amount) throws Exception {
		PaymentRequestPayload payment = new PaymentRequestPayload(tokenId, amount);
		Response response = baseUrl.path("/payments")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(payment));
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new Exception("Server responded with: " + response.getStatus());
		}
	}
}
