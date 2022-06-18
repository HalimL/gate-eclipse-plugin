package de.lmu.gateplugin.model;

public class Similarity {

	private int similarityid;
	private Submission submissionOne;
	private Submission submissionTwo;
	private int percentage;
	private SimilarityTest similarityTest;

	protected Similarity() {
	}

	/**
	 * @param similarityTest
	 * @param submissionOne
	 * @param submissionTwo
	 * @param percentage
	 */
	public Similarity(SimilarityTest similarityTest, Submission submissionOne, Submission submissionTwo,
			int percentage) {
		this.similarityTest = similarityTest;
		this.submissionOne = submissionOne;
		this.submissionTwo = submissionTwo;
		this.percentage = percentage;
	}

	/**
	 * @return the similarityid
	 */
	public int getSimilarityid() {
		return similarityid;
	}

	/**
	 * @param similarityid the similarityid to set
	 */
	public void setSimilarityid(int similarityid) {
		this.similarityid = similarityid;
	}

	/**
	 * @return the percentage
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the submissionOne
	 */
	public Submission getSubmissionOne() {
		return submissionOne;
	}

	/**
	 * @return the submissionTwo
	 */
	public Submission getSubmissionTwo() {
		return submissionTwo;
	}

	/**
	 * @param submissionOne the submissionOne to set
	 */
	protected void setSubmissionOne(Submission submissionOne) {
		this.submissionOne = submissionOne;
	}

	/**
	 * @param submissionTwo the submissionTwo to set
	 */
	protected void setSubmissionTwo(Submission submissionTwo) {
		this.submissionTwo = submissionTwo;
	}

	/**
	 * @return the similarityTest
	 */
	public SimilarityTest getSimilarityTest() {
		return similarityTest;
	}

	/**
	 * @param similarityTest the similarityTest to set
	 */
	public void setSimilarityTest(SimilarityTest similarityTest) {
		this.similarityTest = similarityTest;
	}
}
