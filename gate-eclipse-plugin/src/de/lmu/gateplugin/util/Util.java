package de.lmu.gateplugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

import de.lmu.gateplugin.model.Task;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.ui.pages.AuthorizePage;
import de.lmu.gateplugin.ui.pages.Template;

public class Util {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();

	private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

	public static void showInfoMessageDialog(String message, String statusInfo) {

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), statusInfo, message);
			}
		});
	}

	public static void showFailedAuthorizationMessageDialog(String message, String title, Composite parent,
			Template template) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {

				MessageDialog dialog = new MessageDialog(Display.getDefault().getActiveShell(), title, null, message,
						MessageDialog.ERROR, new String[] { "OK" }, 0);
				int result = dialog.open();

				if (result == 0) {
					AuthorizePage.navigateToLoginPage(parent, template);
				}
			}
		});
	}

	public static void resetPerspective() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
			}
		});
	}

	public static void openPreferenceMenu(Composite parent) {
		String preferenceId = "gate-eclipse-plugin.preference";
		Shell shell = parent.getShell();

		PreferencesUtil.createPreferenceDialogOn(shell, preferenceId, new String[] { preferenceId }, null).open();
	}

	public static String fileToBufferString(InputStream is, IPackageFragment pack) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("package " + pack.getElementName() + ";\n");
		stringBuffer.append("\n");

		StringBuilder stringBuilder = new StringBuilder(stringBuffer);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		for (int c; (c = br.read()) != -1;) {
			stringBuilder.append((char) c);
		}
		return stringBuilder.toString();
	}

	public static void createJavaProject(Task task, InputStream inputStream, IProgressMonitor monitor)
			throws CoreException, IOException {

		String projectName = task.getTitle();
		// create a simple project
		IProject project = WORKSPACE_ROOT.getProject(projectName);
		project.create(monitor);
		project.open(monitor);

		// set the Java nature of the created project
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, monitor);

		// create Java project
		IJavaProject javaProject = JavaCore.create(project);

		// specify the output location of the compiler (the bin folder)
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, monitor);
		javaProject.setOutputLocation(binFolder.getFullPath(), monitor);

		// Define the class path entries. Class path entries define the roots of package
		// fragments
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}

		// add libs to project class path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), monitor);

		// create the source folder
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, monitor);

		// the created source folder should be added to the class entries of the
		// project, otherwise compilation will fail
		IPackageFragmentRoot packageFragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageFragmentRoot.getPath());
		javaProject.setRawClasspath(newEntries, monitor);

		generateJavaClass(javaProject, sourceFolder, inputStream, task, monitor);
	}

	private static void generateJavaClass(IJavaProject javaProject, IFolder sourceFolder, InputStream inputStream,
			Task task, IProgressMonitor monitor) throws JavaModelException, IOException {

		// generate a Java class
		IPackageFragment pack = javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment("mypackage",
				false, null);

		String sourceCode = fileToBufferString(inputStream, pack);
		PLUGIN_INSTANCE.getLogger().info("Gotten source code for: " + task.getFeaturedFiles());
		pack.createCompilationUnit(task.getFeaturedFiles(), sourceCode, false, monitor);
	}

	public static boolean workspaceJavaProjectExists(Task task) {

		IProject workspaceProject = WORKSPACE_ROOT.getProject(task.getTitle());
		return workspaceProject.exists();
	}

	public static void openProjectInEditor(Task task, boolean externalProject) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();

				try {
					PLUGIN_INSTANCE.getLogger().info("Opening project " + task.getTitle() + " in Eclipse...");

					if (externalProject) {

						String projectPath = WORKSPACE_ROOT.getLocation().toString() + "/" + task.getTitle();

						try {
							importExistingProject(projectPath, task);
						} catch (InvocationTargetException e) {
							PLUGIN_INSTANCE.getLogger().warn(e.getMessage());
							e.printStackTrace();
						} catch (InterruptedException e) {
							PLUGIN_INSTANCE.getLogger().warn(e.getMessage());
							e.printStackTrace();
						}

					}

					IPath relativeFilePath = new Path(
							"/" + task.getTitle() + "/src/mypackage/" + task.getFeaturedFiles());
					IFile file = WORKSPACE_ROOT.getFile(relativeFilePath);
					IDE.openEditor(page, file, true);

				} catch (CoreException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
	}

	public static boolean externalJavaProjectExists(Task task) {

		String path = WORKSPACE_ROOT.getLocation().toString() + "/" + task.getTitle();
		File externalProject = new File(path);
		return externalProject.exists();
	}

	public static void importExistingProject(String projectPath, Task task)
			throws CoreException, InvocationTargetException, InterruptedException {

		String projectName = task.getTitle();

		IProject project = WORKSPACE_ROOT.getProject(projectName);
		project.create(null);
		project.open(null);

		IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
			public String queryOverwrite(String file) {
				return ALL;
			}
		};

		ImportOperation importOperation = new ImportOperation(project.getFullPath(), new File(projectPath),
				FileSystemStructureProvider.INSTANCE, overwriteQuery);
		importOperation.setCreateContainerStructure(false);
		importOperation.run(null);
	}

	public static void openTaskFileInEditor(Task task) {

		String workspacePath = WORKSPACE_ROOT.getLocation().toString();

		File dir = new File(workspacePath);
		File[] files = dir.listFiles((d, name) -> name.matches(task.getFilenameRegexp()));
		File taskFile;
		String taskFilePath;
		IFileStore fileStore;

		if (files.length != 0) {
			taskFile = files[0];
			taskFilePath = taskFile.getAbsolutePath();

		} else {
			String regex = "\\\\";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(task.getFilenameRegexp());
			String taskName = matcher.replaceAll("");

			taskFile = new File(workspacePath + "/" + taskName);
			taskFilePath = taskFile.getAbsolutePath();

			try {
				taskFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		fileStore = getFileStore(taskFilePath);
		if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
			}
		}
	}

	public static IFileStore getFileStore(String taskPath) {

		IPath workspacePath = WORKSPACE_ROOT.getFullPath();
		IFileStore fileStore = EFS.getLocalFileSystem().getStore(workspacePath).getChild(taskPath);

		return fileStore;
	}
}
