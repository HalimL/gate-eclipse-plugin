package de.lmu.gateplugin.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.lmu.gateplugin.util.GatePreferenceStore;

public class GatePerspective implements IPerspectiveFactory {

	private static final String LOGIN_VIEW_ID = "de.lmu.gateplugin.LoginView";
	private static final String TASK_VIEW_ID = "de.lmu.gateplugin.TaskView";

	@Override
	public void createInitialLayout(IPageLayout layout) {

		boolean notLoggedIn = (getAccessToken() == null || getRefreshToken() == null);

		if (notLoggedIn) {
			loginPerspective(layout);
		} else {
			taskPerspective(layout);
		}

	}

	public void loginPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.15f, editorArea);
		layout.addView(LOGIN_VIEW_ID, IPageLayout.RIGHT, 0.62f, editorArea);

		IFolderLayout folderLayout = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, LOGIN_VIEW_ID);
		// layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, editorArea);
		folderLayout.addView(IPageLayout.ID_PROBLEM_VIEW);

	}

	public void taskPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.addView(IPageLayout.ID_PROJECT_EXPLORER, IPageLayout.LEFT, 0.15f, editorArea);
		layout.addView(TASK_VIEW_ID, IPageLayout.RIGHT, 0.62f, editorArea);

		IFolderLayout folderLayout = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, TASK_VIEW_ID);
		// layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, editorArea);
		folderLayout.addView(IPageLayout.ID_PROBLEM_VIEW);
	}

	public String getAccessToken() {
		return GatePreferenceStore.getGatePreferenceStore().getAccessToken();
	}

	public String getRefreshToken() {
		return GatePreferenceStore.getGatePreferenceStore().getRefreshToken();
	}

}