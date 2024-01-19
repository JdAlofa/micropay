package dtu.dtupay.common;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequestPayload {
	private UUID tokenId; // Token identifier
	private BigDecimal amount;

	public PaymentRequestPayload() {
	}

	public PaymentRequestPayload(UUID tokenId, BigDecimal amount) {
		this.tokenId = tokenId;
		this.amount = amount;
	}

	public UUID getId() {
		return tokenId;
	}

	public void setId(UUID tokenId) {
		this.tokenId = tokenId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
