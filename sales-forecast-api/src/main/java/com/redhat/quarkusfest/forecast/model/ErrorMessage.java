package com.redhat.quarkusfest.forecast.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ErrorMessage {

	private final int status;
	private final String message;

	@JsonCreator
	public ErrorMessage(@JsonProperty("status") int status, @JsonProperty("message") String message) {
		this.status = status;
		this.message = message;
	}

	@JsonProperty("status")
	public int getStatus() {
		return status;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ErrorMessage [status=" + status + ", message=" + message + "]";
	}
}
