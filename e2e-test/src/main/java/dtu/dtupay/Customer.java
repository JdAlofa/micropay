package dtu.dtupay;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import dtu.dtupay.common.Token;

public class Customer {
	private UUID id;
	private List<Token> tokens = new ArrayList<Token>();
	private String bankAccount;

	public Customer(String bankAccount) {
		id = UUID.randomUUID();
		this.bankAccount = bankAccount;
	}

	public UUID getId() {
		return id;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Token> getTokens(List<Token> tokens) {
		return tokens;
	}
}
