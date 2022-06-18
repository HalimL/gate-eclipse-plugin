package de.lmu.gateplugin.ui.pages;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.lmu.gateplugin.model.Task;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.util.ApiRequestJob;

public class OverviewPage {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();
	private static final ApiRequestJob REQUEST_INSTANCE = ApiRequestJob.getInstance();

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss");

	private ScrolledComposite scrolledCompositeParent;
	private Template template;
	private List<Task> tasks;
	private Font largeFont;
	private Font largeFont2;
	private Font contentFont;
	private Font buttonFont;

	public OverviewPage(ScrolledComposite scrolledCompositeParent, Template template) {
		this.scrolledCompositeParent = scrolledCompositeParent;
		this.template = template;
		this.tasks = PLUGIN_INSTANCE.getAvailableTasks();
		this.largeFont = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT1);
		this.largeFont2 = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT2);
		this.contentFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT1);
		this.buttonFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT2);
	}

	public void buildOverviewPage() {

		scrolledCompositeParent.requestLayout();

		Composite childComposite = new Composite(scrolledCompositeParent, SWT.NONE);
		GridLayout childCompositeLayout = new GridLayout(3, true);

		childComposite.setLayout(childCompositeLayout);
		childComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		childComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		template.buildHeader(childComposite);
		template.buildStatus(childComposite, true);

		Font titleFont = largeFont;

		GridData titleGridData = new GridData(SWT.LEFT, SWT.FILL, true, false, 3, 1);
		titleGridData.verticalIndent = 10;

		Label title = new Label(childComposite, SWT.NONE);
		title.setLayoutData(titleGridData);
		title.setText("Aufgaben");
		title.setEnabled(true);
		title.setFont(titleFont);

		if (this.tasks == null) {
			Job userAvailableTaskJob = REQUEST_INSTANCE.getUserAvailableTasksJob();
			userAvailableTaskJob.setSystem(true);
			userAvailableTaskJob.schedule();

			userAvailableTaskJob.addJobChangeListener(new JobChangeAdapter() {
				public void done(IJobChangeEvent event) {
					if (event.getResult().isOK() || event.getResult().getSeverity() == IStatus.INFO) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								Display.getDefault().timerExec(1000, () -> {
									buildTaskCards(childComposite, PLUGIN_INSTANCE.getAvailableTasks());
									scrolledCompositeParent.setContent(childComposite);
									scrolledCompositeParent
											.setMinSize(childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
									scrolledCompositeParent.setExpandVertical(true);
									scrolledCompositeParent.setExpandHorizontal(true);
									scrolledCompositeParent.setAlwaysShowScrollBars(true);
								});
							}
						});
					}
				}
			});

		} else {
			buildTaskCards(childComposite, this.tasks);
			scrolledCompositeParent.setContent(childComposite);
			scrolledCompositeParent.setMinSize(childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			scrolledCompositeParent.setExpandVertical(true);
			scrolledCompositeParent.setExpandHorizontal(true);
			scrolledCompositeParent.setAlwaysShowScrollBars(true);
		}
	}

	public void buildTaskCards(Composite composite, List<Task> availableTasks) {

		if (availableTasks == null) {

			template.buildFooter(composite);

		} else {

			for (int i = 0; i < availableTasks.size(); i++) {

				Task task = availableTasks.get(i);

				GridData groupGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);
				groupGridData.verticalIndent = 20;

				GridLayout groupLayout = new GridLayout(3, true);

				Group group = new Group(composite, SWT.NONE);
				group.setLayout(groupLayout);
				group.setLayoutData(groupGridData);

				group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

				Font vorlesungFont = contentFont;

				Label vorlesung = new Label(group, SWT.NONE);
				vorlesung.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 3, 1));
				vorlesung.setText(task.getTaskGroup().getLecture().getName());
				vorlesung.setFont(vorlesungFont);

				Label seperator = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
				GridData seperatorGridData = new GridData(SWT.FILL, SWT.TOP, true, true, 3, 1);

				seperator.setLayoutData(seperatorGridData);
				seperator.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
				seperator.setAlignment(SWT.CENTER);

				Font taskcontentFont = contentFont;

				GridData labelGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				labelGridData.verticalIndent = 10;
				GridData textGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
				textGridData.verticalIndent = 10;

				GridData taskNameGridData = new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1);
				taskNameGridData.verticalIndent = 10;

				Font taskNameFont = largeFont2;

				Label taskName = new Label(group, SWT.NONE | SWT.LEAD);
				taskName.setLayoutData(taskNameGridData);
				taskName.setText(task.getTitle());
				taskName.setEnabled(true);
				taskName.setFont(taskNameFont);

				Label label2 = new Label(group, SWT.NONE);
				label2.setText("Start Datum:");
				label2.setLayoutData(labelGridData);
				label2.setFont(taskcontentFont);

				Label taskStatus = new Label(group, SWT.NONE | SWT.LEAD);
				taskStatus.setLayoutData(textGridData);
				taskStatus.setText(task.getStart().format(formatter));
				taskStatus.setEnabled(true);
				taskStatus.setFont(taskcontentFont);

				Label label3 = new Label(group, SWT.NONE);
				label3.setText("End Datum:");
				label3.setLayoutData(labelGridData);
				label3.setFont(taskcontentFont);

				Label taskDeadline = new Label(group, SWT.NONE | SWT.LEAD);
				taskDeadline.setLayoutData(textGridData);
				taskDeadline.setText(task.getDeadline().format(formatter));
				taskDeadline.setEnabled(true);
				taskDeadline.setFont(taskcontentFont);

				GridData showTaskButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, false, 3, 1);

				Button showTaskButton = new Button(group, SWT.NONE);
				showTaskButton.setLayoutData(showTaskButtonGridData);
				showTaskButton.setText("Aufgabe bearbeiten");
				showTaskButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
				showTaskButton.setGrayed(true);
				showTaskButton.setFont(buttonFont);

				Job userSubmissionForTaskJob = REQUEST_INSTANCE.getUserSubmissionForTaskJob(task);
				userSubmissionForTaskJob.addJobChangeListener(new JobChangeAdapter() {
					public void done(IJobChangeEvent event) {
						if (event.getResult().isOK()) {
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									navigateToTaskPage(task, scrolledCompositeParent);
								}
							});
						}
					}
				});

				SelectionAdapter showTaskButtonSelectionAdapter = new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						userSubmissionForTaskJob.setSystem(true);
						userSubmissionForTaskJob.schedule();
					}
				};

				showTaskButton.addSelectionListener(showTaskButtonSelectionAdapter);
				showTaskButton.addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(DisposeEvent e) {
						showTaskButton.removeSelectionListener(showTaskButtonSelectionAdapter);

					}
				});
			}

		}

	}

	public static void navigateToTaskPage(Task task, ScrolledComposite scrolledCompositeParent) {
		if ((scrolledCompositeParent != null) && (!scrolledCompositeParent.isDisposed())) {
			scrolledCompositeParent.dispose();

			Composite composite = Activator.getInstance().getParentComposite();

			ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.V_SCROLL | SWT.H_SCROLL);
			scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

			TaskPage taskPage = new TaskPage(scrolledComposite, PLUGIN_INSTANCE.getTemplate(), task);
			taskPage.buildTaskPage();
		}

	}
}