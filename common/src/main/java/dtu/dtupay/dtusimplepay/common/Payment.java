package dtu.dtupay.dtusimplepay.common;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Payment {
	private String customerId;
	private String merchantId;
	private BigDecimal amount;

	public Payment() {

	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
