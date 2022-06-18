package de.lmu.gateplugin.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

	private String accessToken;
	private String refreshToken;
	private String idToken;
	private String tokenType;
	private String scope;
	private String sessionState;

	private int expiresIn;
	private int refreshExpiresIn;
	private int notBeforePolicy;

	public Token(String accessToken, String refreshToken, String idToken, String tokenType, String scope,
			String sessionState, int expiresIn, int refreshExpiresIn, int notBeforePolicy) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.idToken = idToken;
		this.tokenType = tokenType;
		this.scope = scope;
		this.sessionState = sessionState;
		this.expiresIn = expiresIn;
		this.refreshExpiresIn = refreshExpiresIn;
		this.notBeforePolicy = notBeforePolicy;
	}

	public Token() {
	}

	@JsonProperty("access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty("refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@JsonProperty("id_token")
	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	@JsonProperty("token_type")
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonProperty("scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@JsonProperty("session_state")
	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	@JsonProperty("expires_in")
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@JsonProperty("refresh_expires_in")
	public int getRefreshExpiresIn() {
		return refreshExpiresIn;
	}

	public void setRefreshExpiresIn(int refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}

	@JsonProperty("not-before-policy")
	public int getNotBeforePolicy() {
		return notBeforePolicy;
	}

	public void setNotBeforePolicy(int notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}

}
