package de.lmu.gateplugin.model;

import java.util.Set;

public class Participation {

	private int id;
	private User user;
	private Group group;
	private Lecture lecture;
	private String role = ParticipationRole.NORMAL.toString();
	private Set<Submission> submissions;

	public Participation() {
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the lecture
	 */
	public Lecture getLecture() {
		return lecture;
	}

	/**
	 * @param lecture the lecture to set
	 */
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	private String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	private void setRole(String role) {
		this.role = role;
	}

	public ParticipationRole getRoleType() {
		return ParticipationRole.valueOf(getRole());
	}

	public void setRoleType(ParticipationRole type) {
		setRole(type.toString());
	}

	/**
	 * @return the submissions
	 */
	public Set<Submission> getSubmissions() {
		return submissions;
	}

	/**
	 * @param submissions the submissions to set
	 */
	public void setSubmissions(Set<Submission> submissions) {
		this.submissions = submissions;
	}
}