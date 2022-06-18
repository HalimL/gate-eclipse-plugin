package de.lmu.gateplugin.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {

	private String sub;
	private boolean emailVerified;
	private String name;
	private String preferredUsername;
	private String givenName;
	private String familyName;
	private String email;

	public UserInfo() {
	}

	public UserInfo(String sub, boolean emailVerified, String name, String preferredUsername, String givenName,
			String familyName, String email) {
		super();
		this.sub = sub;
		this.emailVerified = emailVerified;
		this.name = name;
		this.preferredUsername = preferredUsername;
		this.givenName = givenName;
		this.familyName = familyName;
		this.email = email;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	@JsonProperty("email_verified")
	public boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("preferred_username")
	public String getPreferredUsername() {
		return preferredUsername;
	}

	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}

	@JsonProperty("given_name")
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	@JsonProperty("family_name")
	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
