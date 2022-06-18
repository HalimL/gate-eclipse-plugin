package de.lmu.gateplugin.model;

public class PointCategory {

	private int pointcatid;
	private Task task;
	private int points;
	private String description;
	private boolean optional = false;

	public PointCategory(Task task, int points, String description, boolean optional) {
		this.task = task;
		this.points = points;
		this.description = description;
		this.optional = optional;
	}

	public int getPointcatid() {
		return pointcatid;
	}

	/**
	 * @param pointcatid the pointcatid to set
	 */
	public void setPointcatid(int pointcatid) {
		this.pointcatid = pointcatid;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
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
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * @param optional the optional to set
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
}
