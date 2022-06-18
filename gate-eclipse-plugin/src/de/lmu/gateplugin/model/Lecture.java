package de.lmu.gateplugin.model;

import java.util.List;
import java.util.Set;

public class Lecture {

	private int id;
	private String name;
	private int semester;
	private boolean requiresAbhnahme;
	private Set<Participation> participants;
	private List<TaskGroup> taskGroups;
	private Set<Group> groups;
	private String gradingMethod = "";
	private String description = "";
	private boolean allowSelfSubscribe = true;

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
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}

	/**
	 *
	 * @return the human-readable semester
	 */
	public String getReadableSemester() {
		String semester = ((Integer) getSemester()).toString();
		if (getSemester() % 2 != 0) {
			return "WS " + semester.substring(0, 4) + "/" + ((getSemester() - 1) / 10 + 1);
		}
		return "SS " + semester.substring(0, 4);
	}

	/**
	 * @return the participants
	 */
	public Set<Participation> getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(Set<Participation> participants) {
		this.participants = participants;
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
	 * @return the taskGroups public List<TaskGroup> getTaskGroups() { return
	 *         taskGroups; }
	 * 
	 *         /**
	 * @param taskGroups the taskGroups to set
	 */
	public void setTaskGroups(List<TaskGroup> taskGroups) {
		this.taskGroups = taskGroups;
	}

	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the requiresAbhnahme
	 */
	public boolean isRequiresAbhnahme() {
		return requiresAbhnahme;
	}

	/**
	 * @param requiresAbhnahme the requiresAbhnahme to set
	 */
	public void setRequiresAbhnahme(boolean requiresAbhnahme) {
		this.requiresAbhnahme = requiresAbhnahme;
	}

	/**
	 * @return the gradingMethod
	 */
	public String getGradingMethod() {
		return gradingMethod;
	}

	/**
	 * @param gradingMethod the gradingMethod to set
	 */
	public void setGradingMethod(String gradingMethod) {
		this.gradingMethod = gradingMethod;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the allowSelfSubscribe
	 */
	public boolean isAllowSelfSubscribe() {
		return allowSelfSubscribe;
	}

	/**
	 * @param allowSelfSubscribe the allowSelfSubscribe to set
	 */
	public void setAllowSelfSubscribe(boolean allowSelfSubscribe) {
		this.allowSelfSubscribe = allowSelfSubscribe;
	}
}