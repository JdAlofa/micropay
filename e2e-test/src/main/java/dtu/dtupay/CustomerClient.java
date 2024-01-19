package dtu.dtupay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dtu.dtupay.common.Token;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class CustomerClient {

	private Customer user;
	private Client client;
	WebTarget baseUrl;

	public CustomerClient(Customer user) {
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

	public ArrayList<Token> requestTokens(int numberOfTokensRequested) throws Exception {
		if (numberOfTokensRequested <= 0) {
			throw new IllegalArgumentException("Number of tokens requested must be a positive integer");
		}
	
		Response response = baseUrl.path("/tokens/{id}/{numberOfTokensRequested}")
				.resolveTemplate("id", user.getId())
				.resolveTemplate("numberOfTokensRequested", numberOfTokensRequested)
				.request()
				.get();
		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new Exception("Server responded with: " + response.getStatus());
		} else {
			String jsonResponse = response.readEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			List<Token> tokenList = objectMapper.readValue(jsonResponse, new TypeReference<List<Token>>() {
			});

			return (ArrayList<Token>) tokenList;
		}
	}
}
