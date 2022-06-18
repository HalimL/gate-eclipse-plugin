package de.lmu.gateplugin.ui.pages;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Label;

import de.lmu.gateplugin.oauth.AuthConfig;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.util.ApiRequestJob;

public class LoginPage {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();
	private static final ApiRequestJob REQUEST_INSTANCE = ApiRequestJob.getInstance();

	private Composite parent;
	private Template template;
	private Font largeFont;
	private Font contentFont;
	private Font buttonFont;

	public LoginPage(Composite parent, Template template) {
		this.parent = parent;
		this.template = template;
		this.largeFont = Activator.getInstance().getFontRegistry().get(Activator.LARGE_FONT1);
		this.contentFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT1);
		this.buttonFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT2);
	}

	public void buildLoginPage() {

		parent.requestLayout();
		template.buildHeader(parent);
		template.buildStatus(parent, false);

		Font welcomeFont = largeFont;

		GridData welcomeGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		welcomeGridData.verticalIndent = 10;

		Label welcome = new Label(parent, SWT.NONE);
		welcome.setLayoutData(welcomeGridData);
		welcome.setText("Willkommen bei GATE!");
		welcome.setEnabled(true);
		welcome.setFont(welcomeFont);

		Font descriptionFont = contentFont;

		GridData description1GridData = new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 3, 1);
		description1GridData.verticalIndent = 20;

		Label description1 = new Label(parent, SWT.WRAP);
		description1.setLayoutData(description1GridData);
		description1.setText(
				"GATE ist ein E-Assessment-System, das zur Unterstützung des Übungsbetriebs von Programmier-Lehrveranstaltungen entwickelt wurde.");
		description1.setEnabled(true);
		description1.setFont(descriptionFont);

		GridData description2GridData = new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 3, 1);
		description2GridData.verticalIndent = 10;

		Label description2 = new Label(parent, SWT.WRAP);
		description2.setLayoutData(description2GridData);
		description2.setText(
				"Das System kann zum einen Studierende bei der Einreichung von Lösungen durch Self-Assessments zur Überprüfung der Korrektheit unterstützen und zum anderen erleichtert es TutorInnen die Feedbackgabe sowie die Bewertung von studentischen Lösungen.");
		description2.setEnabled(true);
		description2.setFont(descriptionFont);

		Font buttonTextFont = buttonFont;

		GridData gateLoginButtonGridData = new GridData(SWT.LEFT, SWT.FILL, false, false, 3, 1);
		gateLoginButtonGridData.verticalIndent = 20;

		Button gateLoginButton = new Button(parent, SWT.NONE);
		gateLoginButton.setLayoutData(gateLoginButtonGridData);
		gateLoginButton.setText("GATE Login");
		gateLoginButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
		gateLoginButton.setGrayed(true);
		gateLoginButton.setFont(buttonTextFont);
		final SelectionAdapter gateLoginButtonSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				REQUEST_INSTANCE.authorizeDeviceJob(parent);
			}
		};
		gateLoginButton.addSelectionListener(gateLoginButtonSelectionAdapter);

		gateLoginButton.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				gateLoginButton.removeSelectionListener(gateLoginButtonSelectionAdapter);
			}
		});

		template.buildFooter(parent);
	}

	public static void navigateToAuthorizationPage(AuthConfig authConfig, Composite parent) {

		if ((parent != null) && (!parent.isDisposed())) {
			parent.dispose();

			Composite composite = Activator.getInstance().getParentComposite();

			Composite childComposite = new Composite(composite, SWT.NONE);
			childComposite.setLayout(new GridLayout(3, true));
			childComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

			AuthorizePage authorizePage = new AuthorizePage(childComposite, PLUGIN_INSTANCE.getTemplate(), authConfig);
			authorizePage.buildAuthorizePage();
		}
	}
}