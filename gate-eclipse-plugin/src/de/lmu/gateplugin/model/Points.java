package de.lmu.gateplugin.model;

public class Points {

	private Integer points;
	private Integer pointStatus; // 0 = ungraded, 1 = nicht abgenommen, 2 = abnahme nicht bestanden, 3 =
									// abgenommen
	private Participation issuedBy;
	private String publicComment;
	private String internalComment;
	private Integer duplicate;

	/**
	 * @return the points
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * @param minPointStep
	 * @return the points
	 */
	public Integer getPointsByStatus(int minPointStep) {
		if (pointStatus <= PointStatus.ABGENOMMEN_FAILED.ordinal()) {
			return 0;
		}
		return getPlagiarismPoints(minPointStep);
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * @return the issuedBy
	 */
	public Participation getIssuedBy() {
		return issuedBy;
	}

	/**
	 * @param issuedBy the issuedBy to set
	 */
	public void setIssuedBy(Participation issuedBy) {
		this.issuedBy = issuedBy;
	}

	/**
	 * @return the comment
	 */
	public String getPublicComment() {
		return publicComment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setPublicComment(String comment) {
		this.publicComment = comment;
	}

	/**
	 * @return the pointsOk
	 */
	public Boolean getPointsOk() {
		return (pointStatus >= PointStatus.ABGENOMMEN_FAILED.ordinal());
	}

	/**
	 * @return the internalComment
	 */
	public String getInternalComment() {
		return internalComment;
	}

	/**
	 * @param internalComment the internalComment to set
	 */
	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}

	/**
	 * @return the isDupe
	 */
	public Integer getDuplicate() {
		return duplicate;
	}

	/**
	 * @param duplicate null = no dupe, 0 = no points, 1 = for historic reasons,
	 *                  other positive values: divisor for points
	 */
	public void setDuplicate(Integer duplicate) {
		if (duplicate != null && duplicate < 0) {
			duplicate = null;
		}
		this.duplicate = duplicate;
	}

	public int getPlagiarismPoints(int minPointStep) {
		return getPlagiarismPoints(duplicate, getPoints(), minPointStep);
	}

	public static int getPlagiarismPoints(Integer duplicate, int points, int minPointStep) {
		if (duplicate == null) {
			return points;
		} else if (duplicate == 0) {
			return 0;
		} else {
			int divided = points / duplicate;
			if (divided % minPointStep != 0) {
				return minPointStep * (divided / minPointStep);
			}
			return divided;
		}
	}

	/**
	 * @return the pointStatus
	 */
	public Integer getPointStatus() {
		return pointStatus;
	}

	/**
	 * @param pointStatus the pointStatus to set
	 */
	private void setPointStatus(Integer pointStatus) {
		this.pointStatus = pointStatus;
	}

	/**
	 * @param pointStatus the pointStatus to set
	 */

	public void setTypedPointStatus(PointStatus pointStatus) {
		setPointStatus(pointStatus.ordinal());
	}

	public PointStatus getTypedPointStatus() {
		return PointStatus.values()[pointStatus];
	}

	public static enum PointStatus {
		NICHT_BEWERTET, NICHT_ABGENOMMEN, ABGENOMMEN_FAILED, ABGENOMMEN
	}
}
