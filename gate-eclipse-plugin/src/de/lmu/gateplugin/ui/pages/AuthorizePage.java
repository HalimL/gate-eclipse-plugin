package de.lmu.gateplugin.ui.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import de.lmu.gateplugin.oauth.AuthConfig;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.util.ApiRequestJob;
import de.lmu.gateplugin.util.Util;

public class AuthorizePage {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();
	private static final ApiRequestJob REQUEST_INSTANCE = ApiRequestJob.getInstance();

	private Composite parent;
	private Template template;
	private Font largeFont;
	private Font contentFont;
	private Font buttonFont;
	private AuthConfig authConfig;

	public AuthorizePage(Composite parent, Template template, AuthConfig authConfig) {
		this.parent = parent;
		this.template = template;
		this.authConfig = authConfig;

		this.largeFont = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT1);
		this.contentFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT1);
		this.buttonFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT2);
	}

	public void buildAuthorizePage() {

		parent.requestLayout();

		template.buildHeader(parent);
		template.buildStatus(parent, false);

		Font authorizeLabelFont = contentFont;

		GridData authorizeLabel1GridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		authorizeLabel1GridData.verticalIndent = 10;

		Label authorizeLabel1 = new Label(parent, SWT.NONE);
		authorizeLabel1.setLayoutData(authorizeLabel1GridData);
		authorizeLabel1.setText("Gehen Sie auf:");
		authorizeLabel1.setEnabled(true);
		authorizeLabel1.setFont(authorizeLabelFont);

		GridData authorizeLinkGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);

		String link = authConfig.getVerificationUri();

		Link authorizationLink = new Link(parent, SWT.NONE);
		authorizationLink.setLayoutData(authorizeLinkGridData);
		authorizationLink.setText("<a href=\"" + link + "\" style=\"text-decoration:none\">" + link + "</a>");
		authorizationLink.setLinkForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		authorizationLink.setFont(authorizeLabelFont);
		authorizationLink.addListener(SWT.Selection, event -> Program.launch(event.text.toString()));

		GridData authorizeLabel2GridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		authorizeLabel2GridData.verticalIndent = 30;

		Label authorizeLabel2 = new Label(parent, SWT.NONE);
		authorizeLabel2.setLayoutData(authorizeLabel2GridData);
		authorizeLabel2.setText("und geben Sie den Code unten ein, um ihr Device zu autorisieren.");
		authorizeLabel2.setEnabled(true);
		authorizeLabel2.setFont(authorizeLabelFont);

		Font codeFont = largeFont;

		GridData codeGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);

		Text code = new Text(parent, SWT.NONE);
		code.setLayoutData(codeGridData);
		code.setText(authConfig.getUserCode());
		code.setEnabled(true);
		code.setEditable(false);
		code.setFont(codeFont);

		Job pollForTokenAndgetUserInfoJob = REQUEST_INSTANCE.getAccessTokenAndUserInfoJob(authConfig, parent, template);
		pollForTokenAndgetUserInfoJob.setSystem(true);
		pollForTokenAndgetUserInfoJob.schedule(authConfig.getInterval() * 1000);

		Job userAvailableTaskJob = REQUEST_INSTANCE.getUserAvailableTasksJob();
		userAvailableTaskJob.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK() || event.getResult().getSeverity() == IStatus.INFO) {
					Util.resetPerspective();
				}
			}
		});

		pollForTokenAndgetUserInfoJob.addJobChangeListener(new JobChangeAdapter() {

			public void done(IJobChangeEvent event) {

				String eventMessage = event.getResult().getMessage();

				if (event.getResult().getSeverity() == IStatus.ERROR) {
					PLUGIN_INSTANCE.getLogger().info(eventMessage);
					Util.showFailedAuthorizationMessageDialog(eventMessage, String.valueOf(400), parent, template);

				} else if (event.getResult().getSeverity() == IStatus.INFO) {
					PLUGIN_INSTANCE.getLogger().info(eventMessage);

				} else if (event.getResult().isOK()) {

					PLUGIN_INSTANCE.getLogger().info("Gotten user info, now getting available tasks for user...");
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							buildProgressBar();
							Display.getDefault().timerExec(1000, () -> {
								REQUEST_INSTANCE.getUserByEmailJob(parent, template);
								userAvailableTaskJob.setSystem(true);
								userAvailableTaskJob.schedule();
							});
						}
					});
				}
			}
		});

		Font buttonTextFont = buttonFont;

		GridData cancelButtonGridData = new GridData(SWT.LEFT, SWT.FILL, false, false, 3, 1);
		cancelButtonGridData.verticalIndent = 40;

		Button cancelButton = new Button(parent, SWT.NONE);
		cancelButton.setLayoutData(cancelButtonGridData);
		cancelButton.setText("Abbrechen");
		cancelButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		cancelButton.setGrayed(true);
		cancelButton.setFont(buttonTextFont);

		SelectionAdapter backButtonSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pollForTokenAndgetUserInfoJob.cancel();
				navigateToLoginPage(parent, template);
			}
		};
		cancelButton.addSelectionListener(backButtonSelectionAdapter);
		cancelButton.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				cancelButton.removeSelectionListener(backButtonSelectionAdapter);
			}

		});

		template.buildFooter(parent);
	}

	public static void navigateToLoginPage(Composite parent, Template template) {
		if ((parent != null) && (!parent.isDisposed())) {
			parent.dispose();

			Composite composite = Activator.getInstance().getParentComposite();

			Composite childComposite = new Composite(composite, SWT.NONE);
			childComposite.setLayout(new GridLayout(3, true));
			childComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

			LoginPage loginPage = new LoginPage(childComposite, template);
			loginPage.buildLoginPage();
		}
		PLUGIN_INSTANCE.getLogger().info("Canceled Authorization");
	}

	private void buildProgressBar() {
		ProgressBar progressBar = new ProgressBar(parent, SWT.INDETERMINATE);
		progressBar.setBounds(120, 310, 80, 40);

	}

}