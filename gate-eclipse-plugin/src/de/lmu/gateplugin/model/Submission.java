package de.lmu.gateplugin.model;

import java.beans.Transient;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.lmu.gateplugin.util.ZonedDateTimeDeserializer;

public class Submission {

	private int submissionid;
	private Task task;
	private Set<Participation> submitters = new HashSet<>();
	private Points points;
	private Set<TestResult> testResults;
	private Set<Similarity> similarSubmissions;
	private ZonedDateTime lastModified = null;
	private ZonedDateTime closedTime = null;
	private Participation closedBy = null;

	protected Submission() {
	}

	/**
	 * @param task
	 * @param submitter
	 */
	public Submission(Task task, Participation submitter) {
		this.task = task;
		this.submitters.add(submitter);
	}

	/**
	 * @return the testResult
	 */
	public Set<TestResult> getTestResults() {
		return testResults;
	}

	/**
	 * @param testResults
	 */
	public void setTestResults(Set<TestResult> testResults) {
		this.testResults = testResults;
	}

	/**
	 * @return the points
	 */
	public Points getPoints() {
		return points;
	}

	@Transient
	public boolean isPointsVisibleToStudents() {
		if (getTask().getShowPoints() != null && getTask().getShowPoints().isBefore(ZonedDateTime.now())
				&& getPoints() != null && getPoints().getPointStatus() > Points.PointStatus.NICHT_BEWERTET.ordinal()) {
			return true;
		}
		return false;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Points points) {
		this.points = points;
	}

	/**
	 * @return the submitters
	 */
	public Set<Participation> getSubmitters() {
		return submitters;
	}

	/**
	 * @param submitters the submitters to set
	 */
	public void setSubmitters(Set<Participation> submitters) {
		this.submitters = submitters;
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
	 * @return the submissionid
	 */
	public int getSubmissionid() {
		return submissionid;
	}

	/**
	 * @param submissionid the submissionid to set
	 */
	public void setSubmissionid(int submissionid) {
		this.submissionid = submissionid;
	}

	/**
	 * @return the similarSubmissions
	 */
	public Set<Similarity> getSimilarSubmissions() {
		return similarSubmissions;
	}

	/**
	 * @param similarSubmissions the similarSubmissions to set
	 */
	public void setSimilarSubmissions(Set<Similarity> similarSubmissions) {
		this.similarSubmissions = similarSubmissions;
	}

	/*
	 * @Transient public String getSubmitterNames() { StringBuilder sb = new
	 * StringBuilder(); for (Participation submitter : getSubmitters()) {
	 * sb.append("; " + submitter.getUser().getFullName()); } return
	 * sb.substring(2).toString(); }
	 * 
	 * @Transient public String setSubmitterNames() { return ""; }
	 */

	/**
	 * @return the lastModified
	 */
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	public ZonedDateTime getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(ZonedDateTime lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Returns the time, when the students closed the solution as final.
	 *
	 * @return time of the final submission
	 */
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	public ZonedDateTime getClosedTime() {
		return closedTime;
	}

	/**
	 * Sets the time, when the submission was closed by the students.
	 *
	 * @param closedTime time
	 */
	public void setClosedTime(ZonedDateTime closedTime) {
		this.closedTime = closedTime;
	}

	/**
	 * Returns the user id of the student who close the submission.
	 *
	 * @return user id of the student
	 */
	public Participation getClosedBy() {
		return closedBy;
	}

	/**
	 * Sets the user, who finally closed the submission.
	 *
	 * @param closedBy user participation
	 */
	public void setClosedBy(Participation closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * Returns the flag, if the submission is prematurely closed by the students
	 *
	 * @return final submission flag
	 */
	public boolean isClosed() {
		return getClosedBy() != null;
	}
}
