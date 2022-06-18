package de.lmu.gateplugin.model;

public class TestResult {

	private int id;
	private Test test;
	private Submission submission;
	private boolean passedTest;
	private String testOutput = "no output available yet";

	public TestResult() {

	}

	/**
	 * @return the passedTest
	 */
	public boolean getPassedTest() {
		return passedTest;
	}

	/**
	 * @param passedTest the passedTest to set
	 */
	public void setPassedTest(boolean passedTest) {
		this.passedTest = passedTest;
	}

	/**
	 * @return the testOutput
	 */
	public String getTestOutput() {
		return testOutput;
	}

	/**
	 * @param testOutput the testOutput to set
	 */
	public void setTestOutput(String testOutput) {
		this.testOutput = testOutput;
	}

	/**
	 * @return the test
	 */
	public Test getTest() {
		return test;
	}

	/**
	 * @param test the test to set
	 */
	public void setTest(Test test) {
		this.test = test;
	}

	/**
	 * @return the submission
	 */
	public Submission getSubmission() {
		return submission;
	}

	/**
	 * @param submission the submission to set
	 */
	public void setSubmission(Submission submission) {
		this.submission = submission;
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
}
