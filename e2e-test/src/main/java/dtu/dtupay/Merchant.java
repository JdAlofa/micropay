package dtu.dtupay;

import java.util.UUID;

public class Merchant {
	private UUID id;
	private String bankAccount;

	public Merchant(String bankAccount) {
		id = UUID.randomUUID();
		this.bankAccount = bankAccount;
	}

	public UUID getId() {
		return id;
	}

	public String getBankAccount() {
		return bankAccount;
	}
}
