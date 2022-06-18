package de.lmu.gateplugin.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ErrorMessage {

	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage(String message) {
		this.message = message;
	}

	@JsonAlias("error_description")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
