package de.lmu.gateplugin.model;

import java.beans.Transient;
import java.util.Date;
import java.util.Set;

public class User {
	private int uid;
	private String username;
	private String email;
	private String lastName;
	private String firstName;
	private boolean superUser = false;
	private Set<Participation> lectureParticipant;
	private Date lastLoggedIn;

	public User() {
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the lectureParticipant
	 */
	public Set<Participation> getLectureParticipant() {
		return lectureParticipant;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @return the superUser
	 */
	public boolean isSuperUser() {
		return superUser;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param lectureParticipant the lectureParticipant to set
	 */
	public void setLectureParticipant(Set<Participation> lectureParticipant) {
		this.lectureParticipant = lectureParticipant;
	}

	/**
	 * @param superUser the superUser to set
	 */
	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the full name of a user
	 * 
	 * @return the full name string
	 */
	@Transient
	public String getFullName() {
		if (getFirstName().isEmpty()) {
			return getLastName();
		}
		return getLastName() + ", " + getFirstName();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the lastLoggedIn
	 */
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	/**
	 * @param lastLoggedIn the lastLoggedIn to set
	 */
	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
}