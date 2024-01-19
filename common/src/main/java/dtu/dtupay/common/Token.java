package dtu.dtupay.common;

import java.util.UUID;

public class Token {
	private UUID id; // Token identifier
	private Boolean valid;

	public Token() {
		this.valid = true;
		this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}
}
