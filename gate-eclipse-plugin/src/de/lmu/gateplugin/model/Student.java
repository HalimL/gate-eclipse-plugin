package de.lmu.gateplugin.model;

public class Student extends User {

	private int matrikelno;
	private String studiengang;

	public Student() {
	}

	/**
	 * @return the matrikelno
	 */
	public int getMatrikelno() {
		return matrikelno;
	}

	/**
	 * @param matrikelno the matrikelno to set
	 */
	public void setMatrikelno(int matrikelno) {
		this.matrikelno = matrikelno;
	}

	/**
	 * @return the studiengang
	 */
	public String getStudiengang() {
		return studiengang;
	}

	/**
	 * @param studiengang the studiengang to set
	 */
	public void setStudiengang(String studiengang) {
		this.studiengang = studiengang;
	}
}
