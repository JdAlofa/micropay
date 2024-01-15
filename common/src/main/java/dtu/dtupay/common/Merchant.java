package dtu.dtupay.common;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Merchant {
	private String id;

	public Merchant() {

	}

	public Merchant(String id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
