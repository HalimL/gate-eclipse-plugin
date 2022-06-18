package de.lmu.gateplugin.model;

import java.util.List;

public class TaskGroup {

	private int taskGroupId;
	private String title = "";
	private List<Task> tasks;
	private Lecture lecture;

	public TaskGroup() {
	}

	public TaskGroup(String title, Lecture lecture) {
		this.title = title;
		this.lecture = lecture;
	}

	/**
	 * @param title
	 */
	public TaskGroup(String title) {
		this.title = title;
	}

	/**
	 * @return the taskGroupId
	 */
	public int getTaskGroupId() {
		return taskGroupId;
	}

	/**
	 * @param taskGroupId the taskGroupId to set
	 */
	public void setTaskGroupId(int taskGroupId) {
		this.taskGroupId = taskGroupId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
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
}
