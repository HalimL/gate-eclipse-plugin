package de.lmu.gateplugin.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenError {

	private String error;
	private String errorDescription;

	public TokenError() {

	}

	public TokenError(String error, String errorDescription) {
		super();
		this.error = error;
		this.errorDescription = errorDescription;
	}

	@JsonProperty("error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@JsonProperty("error_description")
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

}
