package de.lmu.gateplugin.ui.pages;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import de.lmu.gateplugin.model.Submission;
import de.lmu.gateplugin.model.Task;
import de.lmu.gateplugin.model.Test;
import de.lmu.gateplugin.model.TestResult;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.util.ApiRequestJob;
import de.lmu.gateplugin.util.Util;

public class TaskPage {

	private static final ApiRequestJob REQUEST_INSTANCE = ApiRequestJob.getInstance();

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss");

	private ScrolledComposite scrolledCompositeParent;
	private Task task;
	private Submission submission;
	private TestResult testResult;
	private Template template;
	private ImageRegistry imageRegistry;
	private Font largeFont;
	private Font largeFont2;
	private Font largeFont3;
	private Font contentFont1;
	private Font contentFont2;

	public TaskPage(ScrolledComposite scrolledCompositeParent, Template template, Task task) {
		this.scrolledCompositeParent = scrolledCompositeParent;
		this.task = task;
		this.submission = Activator.getInstance().getSubmission();
		this.testResult = Activator.getInstance().getTestResult();
		this.template = template;
		this.imageRegistry = Activator.getInstance().getImageRegistry();
		this.largeFont = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT1);
		this.largeFont2 = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT2);
		this.largeFont3 = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT3);
		this.contentFont1 = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT1);
		this.contentFont2 = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT2);

	}

	public void buildTaskPage() {

		scrolledCompositeParent.requestLayout();

		Composite childComposite = new Composite(scrolledCompositeParent, SWT.NONE);
		GridLayout childCompositeLayout = new GridLayout(3, true);

		childComposite.setLayout(childCompositeLayout);
		childComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		childComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		template.buildHeader(childComposite);
		template.buildStatus(childComposite, true);

		Job userAvailableTaskJob = REQUEST_INSTANCE.getUserAvailableTasksJob();
		userAvailableTaskJob.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							navigateToOverviewPage();
						}
					});
				}
			}
		});

		buildBackButton(childComposite, userAvailableTaskJob);
		buildTaskCard(childComposite);

		template.buildFooter(childComposite);

		scrolledCompositeParent.setContent(childComposite);
		scrolledCompositeParent.setMinSize(childComposite.computeSize(400, SWT.DEFAULT));
		scrolledCompositeParent.setExpandVertical(true);
		scrolledCompositeParent.setExpandHorizontal(true);
		scrolledCompositeParent.setAlwaysShowScrollBars(true);

	}

	public void buildTaskCard(Composite composite) {

		Font titleFont = largeFont;

		GridData titleGridData = new GridData(SWT.LEFT, SWT.FILL, true, false, 3, 1);
		titleGridData.verticalIndent = 20;

		Label title = new Label(composite, SWT.NONE);
		title.setLayoutData(titleGridData);
		title.setText("Aufgabe \"" + task.getTitle() + "\"");
		title.setEnabled(true);
		title.setFont(titleFont);

		Font labelFont = largeFont3;
		Font contentFont = contentFont2;

		GridData labelGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		labelGridData.verticalIndent = 10;

		GridData contentGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		contentGridData.verticalIndent = 10;

		Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setLayoutData(labelGridData);
		descriptionLabel.setText("Beschreibung:");
		descriptionLabel.setEnabled(true);
		descriptionLabel.setFont(labelFont);

		GridData descriptionGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);

		Label description = new Label(composite, SWT.WRAP);
		description.setLayoutData(descriptionGridData);
		description.setText(task.getDescription());
		description.setEnabled(true);
		description.setFont(contentFont);

		Label startDateLabel = new Label(composite, SWT.NONE);
		startDateLabel.setLayoutData(labelGridData);
		startDateLabel.setText("Startdatum:");
		startDateLabel.setEnabled(true);
		startDateLabel.setFont(labelFont);

		Label startDate = new Label(composite, SWT.NONE);
		startDate.setLayoutData(contentGridData);
		startDate.setText(task.getStart().format(formatter));
		startDate.setEnabled(true);
		startDate.setFont(contentFont);

		Label endDateLabel = new Label(composite, SWT.NONE);
		endDateLabel.setLayoutData(labelGridData);
		endDateLabel.setText("Enddatum:");
		endDateLabel.setEnabled(true);
		endDateLabel.setFont(labelFont);

		Label endDate = new Label(composite, SWT.NONE);
		endDate.setLayoutData(contentGridData);
		endDate.setText(task.getDeadline().format(formatter));
		endDate.setEnabled(true);
		endDate.setFont(contentFont);

		Label maxPointsLabel = new Label(composite, SWT.NONE);
		maxPointsLabel.setLayoutData(labelGridData);
		maxPointsLabel.setText("Max. Punkte:");
		maxPointsLabel.setEnabled(true);
		maxPointsLabel.setFont(labelFont);

		Label maxPoints = new Label(composite, SWT.NONE);
		maxPoints.setLayoutData(contentGridData);
		maxPoints.setText(String.valueOf(task.getMaxPoints()));
		maxPoints.setEnabled(true);
		maxPoints.setFont(contentFont);

		Label statusLabel = new Label(composite, SWT.NONE);
		statusLabel.setLayoutData(labelGridData);
		statusLabel.setText("Status:");
		statusLabel.setEnabled(true);
		statusLabel.setFont(labelFont);

		Label status = new Label(composite, SWT.NONE);
		status.setLayoutData(contentGridData);
		status.setText(submission == null ? "noch nicht bearbeitet"
				: "bearbeitet, letzte änderung am " + submission.getLastModified().format(formatter));
		status.setEnabled(true);
		status.setFont(contentFont);

		GridData openInEditorButtonGridData = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 3, 1);
		openInEditorButtonGridData.verticalIndent = 40;

		Font buttonFont = contentFont2;

		Button openInEditorButton = new Button(composite, SWT.NONE);

		openInEditorButton.setLayoutData(openInEditorButtonGridData);
		openInEditorButton
				.setText(submission == null ? "Aufgabe in Eclipse starten " : "Abgabe bearbeiten              ");

		Image eclipseIcon = imageRegistry.get(Activator.ECLIPSE_IMAGE_ID);
		openInEditorButton.setImage(eclipseIcon);

		openInEditorButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
		openInEditorButton.setFont(buttonFont);

		SelectionAdapter openInEditorSelectionAdapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (Util.workspaceJavaProjectExists(task)) {
					Util.openProjectInEditor(task, false);

				} else if (Util.externalJavaProjectExists(task)) {
					Util.openProjectInEditor(task, true);

				} else if (task.getFeaturedFiles().isEmpty()) {
					Util.openTaskFileInEditor(task);

				} else {

					Job createJavaProjectJob = REQUEST_INSTANCE.createJavaProjectJob(task);

					createJavaProjectJob.setSystem(true);
					createJavaProjectJob.schedule();

					createJavaProjectJob.addJobChangeListener(new JobChangeAdapter() {
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								Util.openProjectInEditor(task, false);
							}
						}
					});
				}
			}
		};

		openInEditorButton.addSelectionListener(openInEditorSelectionAdapter);
		openInEditorButton.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				openInEditorButton.removeSelectionListener(openInEditorSelectionAdapter);
			}
		});

		Button submitButton = new Button(composite, SWT.NONE);

		GridData submitButtonGridData = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 3, 1);
		submitButton.setLayoutData(submitButtonGridData);
		submitButton.setText("Lösung bei GATE abgeben");

		Image gateIcon = imageRegistry.get(Activator.GATE_IMAGE_ID);
		submitButton.setImage(gateIcon);

		submitButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));

		submitButton.setFont(buttonFont);
		SelectionAdapter submitButtonSelectionAdapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IWorkspaceRoot iWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				String relativeFilePath;
				boolean save = false;
				File taskFile = null;

				File dir = new File(iWorkspaceRoot.getLocationURI());

				if (task.getFeaturedFiles().isEmpty()) {

					File[] files = dir.listFiles((d, name) -> name.matches(task.getFilenameRegexp()));

					if (files.length != 0) {
						File fileThatMathchesRegex = files[0];
						relativeFilePath = fileThatMathchesRegex.getAbsolutePath();
						taskFile = new File(relativeFilePath);

						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						IEditorPart editor = page.getActiveEditor();
						save = page.saveEditor(editor, true);
					}

				} else {
					relativeFilePath = iWorkspaceRoot.getLocation().toString() + "/" + task.getTitle()
							+ "/src/mypackage/" + task.getFeaturedFiles();
					taskFile = new File(relativeFilePath);

					// save all dirty editors before submitting the file
					save = IDE.saveAllEditors(new IResource[] { iWorkspaceRoot }, true);
				}

				if (taskFile != null && taskFile.exists()) {

					if (save) {

						Job submitSolutionJob = REQUEST_INSTANCE.submitSolution(taskFile, task);
						submitSolutionJob.setSystem(true);
						submitSolutionJob.schedule();

						Job userSubmissionForTaskJob = REQUEST_INSTANCE.getUserSubmissionForTaskJob(task);
						userSubmissionForTaskJob.addJobChangeListener(new JobChangeAdapter() {
							public void done(IJobChangeEvent event) {
								if (event.getResult().isOK()) {
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											OverviewPage.navigateToTaskPage(task, scrolledCompositeParent);
										}
									});
								}
							}
						});

						submitSolutionJob.addJobChangeListener(new JobChangeAdapter() {
							public void done(IJobChangeEvent event) {
								if (event.getResult().isOK()) {
									Util.showInfoMessageDialog("Ihre Lösung für " + task.getTitle()
											+ " wurde erfolgreich bei GATE abgegeben!", null);
									userSubmissionForTaskJob.setSystem(true);
									userSubmissionForTaskJob.schedule();

								}
							}
						});
					}

				} else {

					Util.showInfoMessageDialog("Keine Lösungsdatei für " + task.getTitle()
							+ " gefunden in ihrem Eclipse workspace. Bitte überprüfen Sie ihrem workspace: "
							+ ResourcesPlugin.getWorkspace().getRoot().getLocation().toString(), null);
				}
			}
		};

		submitButton.addSelectionListener(submitButtonSelectionAdapter);
		submitButton.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				submitButton.removeSelectionListener(submitButtonSelectionAdapter);
			}
		});

		buildTestSection(composite);

	}

	public void buildBackButton(Composite parent, Job scheduledJob) {

		GridData backButtonGridData = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);

		Button backButton = new Button(parent, SWT.NONE);
		backButton.setLayoutData(backButtonGridData);

		Image backIcon = imageRegistry.get(Activator.BACK_IMAGE_ID);
		backButton.setImage(backIcon);

		backButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		backButton.setGrayed(true);
		SelectionAdapter backButtonSelectionAdapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				scheduledJob.setSystem(true);
				scheduledJob.schedule();
			}
		};

		backButton.addSelectionListener(backButtonSelectionAdapter);
		backButton.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				backButton.removeSelectionListener(backButtonSelectionAdapter);
			}
		});
	}

	public void navigateToOverviewPage() {

		if ((scrolledCompositeParent != null) && (!scrolledCompositeParent.isDisposed())) {
			scrolledCompositeParent.dispose();

			Composite composite = Activator.getInstance().getParentComposite();

			ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.V_SCROLL | SWT.H_SCROLL);
			scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

			OverviewPage overviewPage = new OverviewPage(scrolledComposite, template);
			overviewPage.buildOverviewPage();

		}
	}

	public void buildTestSection(Composite composite) {

		if (submission != null) {
			Set<Test> testsSet = task.getTests();

			if (!testsSet.isEmpty()) {
				List<Test> testList = new ArrayList<>(testsSet).stream()
						.filter(test -> (test.getTimesRunnableByStudents() != 0)).collect(Collectors.toList());

				Font testsFont = largeFont2;

				GridData testsGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);

				Label testsLabel = new Label(composite, SWT.NONE);
				testsLabel.setLayoutData(testsGridData);
				testsLabel.setText("Mögliche Tests:");
				testsLabel.setEnabled(true);

				testsLabel.setFont(testsFont);

				Label seperator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				GridData seperatorGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);

				seperator.setLayoutData(seperatorGridData);
				seperator.setAlignment(SWT.CENTER);

				buildTestResult(composite);
				buildTestButtons(testList, composite);
			}
		}

	}

	public void buildTestButtons(List<Test> tests, Composite composite) {

		for (int i = 0; i < tests.size(); i++) {

			Test test = tests.get(i);

			Font buttonFont = contentFont2;

			Button testButton = new Button(composite, SWT.NONE);
			GridData testButtonGridData = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 3, 1);

			testButton.setLayoutData(testButtonGridData);
			testButton.setText(test.getTestTitle() + " ausführen");

			Image testIcon = imageRegistry.get(Activator.TEST_IMAGE_ID);
			testButton.setImage(testIcon);

			testButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
			testButton.setEnabled(true);
			testButton.setFont(buttonFont);

			SelectionAdapter testButtonSelectionAdapter = new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					// run test through API
					Job executeTestJob = REQUEST_INSTANCE.executeTestJob(test);
					executeTestJob.addJobChangeListener(new JobChangeAdapter() {
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										Display.getDefault().timerExec(1000, () -> {
											OverviewPage.navigateToTaskPage(task, scrolledCompositeParent);
										});
									}
								});
							}
						}
					});

					executeTestJob.setUser(true);
					executeTestJob.schedule();
				}
			};

			testButton.addSelectionListener(testButtonSelectionAdapter);
			testButton.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					testButton.removeSelectionListener(testButtonSelectionAdapter);
				}
			});

		}
	}

	public void buildTestResult(Composite composite) {

		if (testResult != null && testResult.getSubmission().getSubmissionid() == submission.getSubmissionid()) {

			GridData testLabelGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			Font labelFont = contentFont1;

			Font contentFont = contentFont2;

			Label bestandenLabel = new Label(composite, SWT.NONE);
			bestandenLabel.setLayoutData(testLabelGridData);
			bestandenLabel.setText(testResult.getTest().getTestTitle() + " bestanden:");
			bestandenLabel.setEnabled(true);
			bestandenLabel.setFont(labelFont);

			GridData testContentGridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1);

			Label testStatus = new Label(composite, SWT.NONE);
			testStatus.setLayoutData(testContentGridData);
			testStatus.setText(testResult.getPassedTest() ? "Ja" : "Nein");
			testStatus.setEnabled(true);
			testStatus.setFont(contentFont);
			testStatus.setForeground(
					testResult.getPassedTest() ? Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN)
							: Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));

			Label outputLabel = new Label(composite, SWT.NONE);
			outputLabel.setLayoutData(testLabelGridData);
			outputLabel.setText("Ausgabe:");
			outputLabel.setEnabled(true);
			outputLabel.setFont(labelFont);

			GridData testOutputGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 2);

			Label testOutput = new Label(composite, SWT.WRAP);
			testOutput.setLayoutData(testOutputGridData);
			testOutput.setText(testResult.getTestOutput());
			testOutput.setEnabled(true);
			testOutput.setFont(contentFont);

		}
	}
}