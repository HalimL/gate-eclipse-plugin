package de.lmu.gateplugin.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthConfig {

	private String deviceCode;
	private String userCode;
	private String verificationUri;
	private String verificationUriComplete;
	private int expiresIn;
	private int interval;

	public AuthConfig() {
	}

	public AuthConfig(String deviceCode, String userCode, String verificationUri, String verificationUriComplete,
			int expiresIn, int interval) {
		super();
		this.deviceCode = deviceCode;
		this.userCode = userCode;
		this.verificationUri = verificationUri;
		this.verificationUriComplete = verificationUriComplete;
		this.expiresIn = expiresIn;
		this.interval = interval;
	}

	@JsonProperty("device_code")
	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	@JsonProperty("user_code")
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@JsonProperty("verification_uri")
	public String getVerificationUri() {
		return verificationUri;
	}

	public void setVerificationUri(String verificationUri) {
		this.verificationUri = verificationUri;
	}

	@JsonProperty("verification_uri_complete")
	public String getVerificationUriComplete() {
		return verificationUriComplete;
	}

	public void setVerificationUriComplete(String verificationUriComplete) {
		this.verificationUriComplete = verificationUriComplete;
	}

	@JsonProperty("expires_in")
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}