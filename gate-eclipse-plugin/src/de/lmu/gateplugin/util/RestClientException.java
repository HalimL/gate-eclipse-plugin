package de.lmu.gateplugin.util;

import de.lmu.gateplugin.model.ErrorMessage;
import de.lmu.gateplugin.oauth.TokenError;

public class RestClientException extends Exception {

	ErrorMessage errorMessage;
	TokenError tokenError;
	String statusInfo;

	public RestClientException(TokenError tokenError, String statusInfo) {
		super();
		this.tokenError = tokenError;
		this.statusInfo = statusInfo;
	}

	public RestClientException(ErrorMessage errorMessage, String statusInfo) {
		super();
		this.errorMessage = errorMessage;
		this.statusInfo = statusInfo;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public TokenError getTokenError() {
		return tokenError;
	}

	public void setTokenError(TokenError tokenError) {
		this.tokenError = tokenError;
	}
}
