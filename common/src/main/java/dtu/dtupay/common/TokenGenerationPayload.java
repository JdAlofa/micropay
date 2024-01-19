package dtu.dtupay.common;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement
public class TokenGenerationPayload {
	private List<Token> tokens = new ArrayList<>();

	public TokenGenerationPayload() {

	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Token> getTokens() {
		return tokens;
	}
}
