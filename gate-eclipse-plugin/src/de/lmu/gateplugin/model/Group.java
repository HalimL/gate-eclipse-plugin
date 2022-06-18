package de.lmu.gateplugin.model;

import java.util.Set;

public class Group {

	private int gid;
	private String name;
	private Lecture lecture;
	private Set<Participation> members;
	private Set<Participation> tutors;
	private boolean allowStudentsToSignup = false;
	private boolean allowStudentsToQuit = false;
	private int maxStudents = 20;
	private boolean submissionGroup = false;
	private boolean membersVisibleToStudents = false;

	/**
	 * @return the gid
	 */
	public int getGid() {
		return gid;
	}

	/**
	 * @param gid the gid to set
	 */
	public void setGid(int gid) {
		this.gid = gid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the members
	 */
	public Set<Participation> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(Set<Participation> members) {
		this.members = members;
	}

	/**
	 * @return the allowStudentsToSignup
	 */
	public boolean isAllowStudentsToSignup() {
		return allowStudentsToSignup;
	}

	/**
	 * @param allowStudentsToSignup the allowStudentsToSignup to set
	 */
	public void setAllowStudentsToSignup(boolean allowStudentsToSignup) {
		this.allowStudentsToSignup = allowStudentsToSignup;
	}

	/**
	 * @return the allowStudentsToQuit
	 */
	public boolean isAllowStudentsToQuit() {
		return allowStudentsToQuit;
	}

	/**
	 * @param allowStudentsToQuit the allowStudentsToQuit to set
	 */
	public void setAllowStudentsToQuit(boolean allowStudentsToQuit) {
		this.allowStudentsToQuit = allowStudentsToQuit;
	}

	/**
	 * @return the maxStudents
	 */
	public int getMaxStudents() {
		return maxStudents;
	}

	/**
	 * @param maxStudents the maxStudents to set
	 */
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	/**
	 * @return the tutors
	 */
	public Set<Participation> getTutors() {
		return tutors;
	}

	/**
	 * @param tutors the tutors to set
	 */
	public void setTutors(Set<Participation> tutors) {
		this.tutors = tutors;
	}

	/**
	 * @return the submissionGroup
	 */
	public boolean isSubmissionGroup() {
		return submissionGroup;
	}

	/**
	 * @param submissionGroup the submissionGroup to set
	 */
	public void setSubmissionGroup(boolean submissionGroup) {
		this.submissionGroup = submissionGroup;
	}

	/**
	 * @return the membersVisibleToStudents
	 */
	public boolean isMembersVisibleToStudents() {
		return membersVisibleToStudents;
	}

	/**
	 * @param membersVisibleToStudents the membersVisibleToStudents to set
	 */
	public void setMembersVisibleToStudents(boolean membersVisibleToStudents) {
		this.membersVisibleToStudents = membersVisibleToStudents;
	}
}