package dtu.dtupay;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import dtu.dtupay.common.Token;

public class Customer {
	private UUID id;
	private String name;
	private List<Token> tokens = new ArrayList<Token>();
	private String bankAccount;

	public Customer(String name, String bankAccount) {
		id = UUID.randomUUID();
		this.name = name;
		this.bankAccount = bankAccount;
	}

	public UUID getId() {
		return id;
	}

	public String getBankAccount() {
		return bankAccount;
	}
}
