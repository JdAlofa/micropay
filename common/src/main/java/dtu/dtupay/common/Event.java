package dtu.dtupay.common;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Event {
	private UUID uuid;
	private String type;
	private String payload;

	public Event() {
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(Object payload) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String serializedPayload = mapper.writeValueAsString(payload);
		this.payload = serializedPayload;
	}
}
