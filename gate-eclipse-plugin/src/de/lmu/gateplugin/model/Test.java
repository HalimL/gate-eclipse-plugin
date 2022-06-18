package de.lmu.gateplugin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Test {

	private int id;
	private int timesRunnableByStudents = 0;
	private boolean forTutors = false;
	private Task task;
	private int timeout = 5;
	private String testTitle = "";
	private String testDescription = "";
	private boolean needsToRun = true;
	private boolean giveDetailsToStudents = false;

	public Test() {
	}

	/**
	 * @return the visibleToStudents
	 */
	public int getTimesRunnableByStudents() {
		return timesRunnableByStudents;
	}

	/**
	 * @param timesRunnableByStudents
	 */
	public void setTimesRunnableByStudents(int timesRunnableByStudents) {
		this.timesRunnableByStudents = timesRunnableByStudents;
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
	 * @return the task
	 */
	@JsonIgnore
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
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the forTutors
	 */
	public boolean isForTutors() {
		return forTutors;
	}

	/**
	 * @param forTutors the forTutors to set
	 */
	public void setForTutors(boolean forTutors) {
		this.forTutors = forTutors;
	}

	/**
	 * @return the testTitle
	 */
	public String getTestTitle() {
		return testTitle;
	}

	/**
	 * @param testTitle the testTitle to set
	 */
	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	/**
	 * @return the testDescription
	 */
	public String getTestDescription() {
		return testDescription;
	}

	/**
	 * @param testDescription the testDescription to set
	 */
	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	/**
	 * @param needsToRun the needsToRun to set
	 */
	public void setNeedsToRun(boolean needsToRun) {
		this.needsToRun = needsToRun;
	}

	/**
	 * @return the needsToRun
	 */
	public boolean isNeedsToRun() {
		return needsToRun;
	}

	/*
	 * @Transient abstract public AbstractTest getTestImpl();
	 */
	/**
	 * @return the giveDetailsToStudents
	 */
	public boolean isGiveDetailsToStudents() {
		return giveDetailsToStudents;
	}

	/**
	 * @param giveDetailsToStudents the giveDetailsToStudents to set
	 */
	public void setGiveDetailsToStudents(boolean giveDetailsToStudents) {
		this.giveDetailsToStudents = giveDetailsToStudents;
	}

	/*
	 * @Transient abstract public boolean TutorsCanRun();
	 */
}
