package de.lmu.gateplugin.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import de.lmu.gateplugin.ui.pages.OverviewPage;

public class TaskView extends ViewPart {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		PLUGIN_INSTANCE.setParentComposite(parent);

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		OverviewPage overviewPage = new OverviewPage(scrolledComposite, PLUGIN_INSTANCE.getTemplate());
		overviewPage.buildOverviewPage();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}