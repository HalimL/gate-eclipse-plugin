package de.lmu.gateplugin.ui.pages;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.util.GatePreferenceStore;

public class Template {

	private static final GatePreferenceStore GATE_PREFERENCE_STORE = GatePreferenceStore.getGatePreferenceStore();
	private static Template instance;

	private ImageRegistry imageRegistry;
	private Font contentFont;

	public Template() {
		imageRegistry = Activator.getInstance().getImageRegistry();
		contentFont = Activator.getInstance().getFontRegistry().get(Activator.CONTENT_FONT3);
	}

	public static Template getInstance() {
		if (instance == null) {
			instance = new Template();
		}
		return instance;
	}

	public void buildHeader(Composite parent) {

		GridData headerGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 4);
		headerGridData.heightHint = 100;

		Label header = new Label(parent, SWT.CENTER);
		header.setText("Header");
		Image headerImage = imageRegistry.get(Activator.HEADER_IMAGE_ID);
		header.setImage(headerImage);
		header.setLayoutData(headerGridData);
		header.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

	}

	public void buildStatus(Composite parent, boolean loggedIn) {

		Font statusFont = contentFont;

		GridData statusGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);

		String status = loggedIn ? "Angemeldet als: " + GATE_PREFERENCE_STORE.getName() : "nicht eingeloggt";

		Label statusLabel = new Label(parent, SWT.NONE);
		statusLabel.setLayoutData(statusGridData);
		statusLabel.setText(status);
		statusLabel.setEnabled(true);

		statusLabel.setFont(statusFont);

		Label seperator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData seperatorGridData = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);

		seperator.setLayoutData(seperatorGridData);
		seperator.setAlignment(SWT.CENTER);
	}

	public void buildFooter(Composite parent) {
		Label seperator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);

		GridData seperatorGridData = new GridData(SWT.FILL, SWT.BOTTOM, true, true, 3, 1);

		seperator.setLayoutData(seperatorGridData);
		seperator.setAlignment(SWT.CENTER);

		Font footerFont = contentFont;

		Link link1 = new Link(parent, SWT.NONE);
		link1.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
		link1.setText("<a href=\"https://" + "gate.ifi.lmu.de/submissionsystem/legal.jsp#imprint"
				+ "\" style=\"text-decoration:none\">" + "Impressum" + "</a>");
		link1.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		link1.setLinkForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		link1.setFont(footerFont);
		link1.addListener(SWT.Selection, event -> Program.launch(event.text.toString()));

		Link link2 = new Link(parent, SWT.NONE);
		link2.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 1, 1));
		link2.setText("<a href=\"https://" + "gate.ifi.lmu.de/submissionsystem/legal.jsp#tos"
				+ "\" style=\"text-decoration:none\">" + "Nutzungsbedingungen" + "</a>");
		link2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		link2.setLinkForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		link2.setFont(footerFont);
		link2.addListener(SWT.Selection, event -> Program.launch(event.text.toString()));

		Link link3 = new Link(parent, SWT.NONE);
		link3.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
		link3.setText("<a href=\"https://" + "gate.ifi.lmu.de/submissionsystem/legal.jsp#data-protection"
				+ "\" style=\"text-decoration:none\">" + "Datenschutz" + "</a>");

		link3.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		link3.setLinkForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		link3.setFont(footerFont);
		link3.addListener(SWT.Selection, event -> Program.launch(event.text.toString()));

	}

}