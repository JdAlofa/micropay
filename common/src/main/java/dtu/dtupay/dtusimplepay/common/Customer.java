package dtu.dtupay.dtusimplepay.common;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {
	private String id;

	public Customer() {

	}

	public Customer(String id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
