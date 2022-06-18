package de.lmu.gateplugin.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import de.lmu.gateplugin.ui.pages.LoginPage;

public class LoginView extends ViewPart {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();

	public LoginView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		PLUGIN_INSTANCE.setParentComposite(parent);

		GridLayout gridLayout = new GridLayout(3, true);
		parent.setLayout(gridLayout);

		Composite childComposite = new Composite(parent, SWT.NONE);
		childComposite.setLayout(gridLayout);
		childComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		LoginPage loginPage = new LoginPage(childComposite, PLUGIN_INSTANCE.getTemplate());
		loginPage.buildLoginPage();

	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}
