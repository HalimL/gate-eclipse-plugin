package de.lmu.gateplugin.model;

public class SimilarityTest {

	private int similarityTestId;
	private Task task;
	private int minimumDifferenceInPercent = 50;
	private int status = 1; // 1 = needs to run, 2 = running, 0 = done
	private String type;
	private String basis;
	private boolean normalizeCapitalization;
	private String tabsSpacesNewlinesNormalization;
	private String excludeFiles;

	/**
	 * @param task
	 * @param type
	 * @param basis
	 * @param normalizeCapitalization
	 * @param tabsSpacesNewlinesNormalization
	 * @param minimumDifferenceInPercent
	 * @param excludeFiles                    comma separated list of files to
	 *                                        exclude
	 */
	public SimilarityTest(Task task, String type, String basis, boolean normalizeCapitalization,
			String tabsSpacesNewlinesNormalization, int minimumDifferenceInPercent, String excludeFiles) {
		this.task = task;
		this.type = type;
		this.basis = basis;
		this.normalizeCapitalization = normalizeCapitalization;
		this.tabsSpacesNewlinesNormalization = tabsSpacesNewlinesNormalization;
		setMinimumDifferenceInPercent(minimumDifferenceInPercent);
		setExcludeFiles(excludeFiles);
	}

	/**
	 * @return the similarityTestId
	 */
	public int getSimilarityTestId() {
		return similarityTestId;
	}

	/**
	 * @param similarityTestId the similarityTestId to set
	 */
	public void setSimilarityTestId(int similarityTestId) {
		this.similarityTestId = similarityTestId;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the basis
	 */
	public String getBasis() {
		return basis;
	}

	/**
	 * @param basis the basis to set
	 */
	public void setBasis(String basis) {
		this.basis = basis;
	}

	/**
	 * @return the normalizeCapitalization
	 */
	public boolean isNormalizeCapitalization() {
		return normalizeCapitalization;
	}

	/**
	 * @param normalizeCapitalization the normalizeCapitalization to set
	 */
	public void setNormalizeCapitalization(boolean normalizeCapitalization) {
		this.normalizeCapitalization = normalizeCapitalization;
	}

	/**
	 * @return the tabsSpacesNewlinesNormalization
	 */
	public String getTabsSpacesNewlinesNormalization() {
		return tabsSpacesNewlinesNormalization;
	}

	/**
	 * @param tabsSpacesNewlinesNormalization the tabsSpacesNewlinesNormalization to
	 *                                        set
	 */
	public void setTabsSpacesNewlinesNormalization(String tabsSpacesNewlinesNormalization) {
		this.tabsSpacesNewlinesNormalization = tabsSpacesNewlinesNormalization;
	}

	/**
	 * @return the minimumDifferenceInPercent
	 */
	public int getMinimumDifferenceInPercent() {
		return minimumDifferenceInPercent;
	}

	/**
	 * @param minimumDifferenceInPercent the minimumDifferenceInPercent to set
	 */
	public void setMinimumDifferenceInPercent(int minimumDifferenceInPercent) {
		if (minimumDifferenceInPercent < 0 || minimumDifferenceInPercent > 100) {
			this.minimumDifferenceInPercent = 50;
		} else {
			this.minimumDifferenceInPercent = minimumDifferenceInPercent;
		}
	}

	/**
	 * Returns the configured plagiarism test
	 * 
	 * @param path the path to the submissions
	 * @return the dupecheck instance or null
	 */
	/*
	 * @Transient public DupeCheck getDupeCheck(File path) { if
	 * ("plaggie".equals(getType())) { return new PlaggieAdapter(path); } else if
	 * ("jplag".equals(getType())) { return new JPlagAdapter(path); } else if
	 * ("compression".equals(getType())) { return new CompressionDistance(path); }
	 * else if ("levenshtein".equals(getType())) { return new
	 * LevenshteinDistance(path); } else { return null; } }
	 */

	/**
	 * Returns the configured normalizer
	 * 
	 * @return the normalizer
	 */
	/*
	 * @Transient public NormalizerIf getNormalizer() { NormalizerStack normalizer =
	 * new NormalizerStack(); int normalizers = 0; if ("code".equals(getBasis())) {
	 * normalizer.addNormalizer(new StripCommentsNormalizer()); normalizers++; }
	 * else if ("comments".equals(getBasis())) { normalizer.addNormalizer(new
	 * StripCommentsNormalizer()); normalizers++; } if (isNormalizeCapitalization())
	 * { normalizer.addNormalizer(new CapitalizationNormalizer()); normalizers++; }
	 * if ("newlines".equals(getTabsSpacesNewlinesNormalization())) {
	 * normalizer.addNormalizer(new NewlinesNormalizer()); normalizers++; } else if
	 * ("tabsspaces".equals(getTabsSpacesNewlinesNormalization())) {
	 * normalizer.addNormalizer(new SpacesTabsNormalizer()); normalizers++; } else
	 * if ("all".equals(getTabsSpacesNewlinesNormalization())) {
	 * normalizer.addNormalizer(new SpacesTabsNewlinesNormalizer()); normalizers++;
	 * } if (normalizers == 0) { normalizer.addNormalizer(new NullNormalizer()); }
	 * return normalizer; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if ("jplag".equals(getType())) {
			return "JPlag-Test, min. Übereinstimmung: " + getMinimumDifferenceInPercent() + "%";
		}
		if ("plaggie".equals(getType())) {
			return "Plaggie-Test (Java <= 1.6), min. Übereinstimmung: " + getMinimumDifferenceInPercent() + "%";
		}
		String string;
		if ("compression".equals(getType())) {
			string = "Kolmogorov Komplexität";
		} else if ("levenshtein".equals(getType())) {
			string = "Levenshtein";
		} else {
			string = "unbekannter Typ";
		}
		string += ", min. Übereinstimmung: " + getMinimumDifferenceInPercent() + "%, ";
		if ("code".equals(getBasis())) {
			string += "nur Code-Basis, ";
		} else if ("comments".equals(getBasis())) {
			string += "nur Kommentar-Basis, ";
		}
		if (isNormalizeCapitalization()) {
			string += "case-insensitive, ";
		}
		if ("newlines".equals(getTabsSpacesNewlinesNormalization())) {
			string += "Newline Normalisierung";
		} else if ("spacestabs".equals(getTabsSpacesNewlinesNormalization())) {
			string += "Spaces/Tabs Normalisierung";
		} else if ("all".equals(getTabsSpacesNewlinesNormalization())) {
			string += "komplette Normalisierung";
		} else {
			string += "ohne Normalisierung";
		}
		return string;
	}

	/**
	 * @return the excludeFiles
	 */
	public String getExcludeFiles() {
		return excludeFiles;
	}

	/**
	 * @param excludeFiles the excludeFiles to set
	 */
	public void setExcludeFiles(String excludeFiles) {
		if (excludeFiles != null) {
			String[] excludedFiles = excludeFiles.split(",");
			excludeFiles = "";
			for (String fileName : excludedFiles) {
				fileName = fileName.trim();
				if (!fileName.isEmpty()) {
					if (excludeFiles.isEmpty()) {
						excludeFiles = fileName;
					} else {
						excludeFiles += "," + fileName;
					}
				}
			}
			this.excludeFiles = excludeFiles;
		} else {
			this.excludeFiles = "";
		}
	}
}
