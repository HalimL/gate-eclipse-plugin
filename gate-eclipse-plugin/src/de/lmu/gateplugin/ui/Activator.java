package de.lmu.gateplugin.ui;

import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import de.lmu.gateplugin.model.Submission;
import de.lmu.gateplugin.model.Task;
import de.lmu.gateplugin.model.TestResult;
import de.lmu.gateplugin.ui.pages.Template;
import de.lmu.gateplugin.util.GatePreferenceStore;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements EventHandler {

	public static final String PLUGIN_ID = "de.lmu.gateplugin";
	public static final String IMAGES_PATH = "icons/";

	public static final String HEADER_IMAGE_ID = "header.image";
	public static final String ECLIPSE_IMAGE_ID = "eclipse.image";
	public static final String GATE_IMAGE_ID = "gate.image";
	public static final String TEST_IMAGE_ID = "test.image";
	public static final String BACK_IMAGE_ID = "back.image";

	public static final String LARGE_FONT1 = "large.font1";
	public static final String LARGE_FONT2 = "large.font2";
	public static final String LARGE_FONT3 = "large.font3";
	public static final String CONTENT_FONT1 = "content.font1";
	public static final String CONTENT_FONT2 = "content.font2";
	public static final String CONTENT_FONT3 = "content.font3";

	private static Activator pluginInstance;

	private Template template;
	private FontRegistry fontRegistry;
	private List<Task> availableTasks;
	private Submission submission;
	private TestResult testResult;

	public Composite parentComposite;

	public static Activator getInstance() {
		return pluginInstance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		pluginInstance = this;
		fillConfiguration();
		subscribeToEventBroker();
		getLogger().info("Started GATE");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		getLogger().info("Shutting down GATE");
		pluginInstance = null;
		super.stop(context);
	}

	public void fillConfiguration() {
		GatePreferenceStore.getGatePreferenceStore();
		initializeFontRegistry();
		this.template = Template.getInstance();
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		Bundle bundle = getInstance().getBundle();

		String imageName = "header.png";
		Path imagePath = new Path(Activator.IMAGES_PATH + imageName);
		URL imageUrl = FileLocator.find(bundle, imagePath, null);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
		registry.put(HEADER_IMAGE_ID, imageDescriptor);

		imageName = "left.png";
		imagePath = new Path(Activator.IMAGES_PATH + imageName);
		imageUrl = FileLocator.find(bundle, imagePath, null);
		imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
		registry.put(BACK_IMAGE_ID, imageDescriptor);

		imageName = "sample.png";
		imagePath = new Path(Activator.IMAGES_PATH + imageName);
		imageUrl = FileLocator.find(bundle, imagePath, null);
		imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
		registry.put(ECLIPSE_IMAGE_ID, imageDescriptor);

		imageName = "gate_icon.png";
		imagePath = new Path(Activator.IMAGES_PATH + imageName);
		imageUrl = FileLocator.find(bundle, imagePath, null);
		imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
		registry.put(GATE_IMAGE_ID, imageDescriptor);

		imageName = "play_icon.png";
		imagePath = new Path(Activator.IMAGES_PATH + imageName);
		imageUrl = FileLocator.find(bundle, imagePath, null);
		imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
		registry.put(TEST_IMAGE_ID, imageDescriptor);

	}

	public void initializeFontRegistry() {

		fontRegistry = new FontRegistry(PlatformUI.getWorkbench().getDisplay());

		fontRegistry.put(LARGE_FONT1, new FontData[] { new FontData("Times New Roman", 20, SWT.NORMAL) });
		fontRegistry.put(LARGE_FONT2, new FontData[] { new FontData("Times New Roman", 18, SWT.NORMAL) });
		fontRegistry.put(LARGE_FONT3, new FontData[] { new FontData("Times New Roman", 15, SWT.NORMAL) });

		fontRegistry.put(CONTENT_FONT1, new FontData[] { new FontData("Times New Roman", 14, SWT.NORMAL) });
		fontRegistry.put(CONTENT_FONT2, new FontData[] { new FontData("Times New Roman", 13, SWT.NORMAL) });
		fontRegistry.put(CONTENT_FONT3, new FontData[] { new FontData("Times New Roman", 12, SWT.NORMAL) });
	}

	public Composite getParentComposite() {
		return parentComposite;
	}

	public void setParentComposite(Composite parentComposite) {
		this.parentComposite = parentComposite;
	}

	public Template getTemplate() {
		return template;
	}

	public FontRegistry getFontRegistry() {
		return fontRegistry;
	}

	public ILog getLogger() {
		return Platform.getLog(pluginInstance.getBundle());
	}

	public List<Task> getAvailableTasks() {
		return availableTasks;
	}

	public Submission getSubmission() {
		return submission;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setAvailableTasks(List<Task> availableTasks) {
		this.availableTasks = availableTasks;
	}

	public void setUserSubmissionForTask(Submission submission) {
		this.submission = submission;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public void subscribeToEventBroker() {
		Object service = PlatformUI.getWorkbench().getService(IEventBroker.class);
		if (service instanceof IEventBroker)
			((IEventBroker) service).subscribe("com/example/eventbroker/advanced", this);
		getLogger().info("Subsrcibe event broker");
	}

	@Override
	public void handleEvent(Event event) {
		getLogger().info("Event with topic: " + event.getTopic() + " came in");
		Object object = event.getProperty("org.eclipse.e4.data");

		if (object instanceof List<?>) {
			setAvailableTasks((List<Task>) object);

		} else if (object instanceof Submission || object == null) {
			setUserSubmissionForTask((Submission) object);

		} else if (object instanceof TestResult) {
			setTestResult((TestResult) object);
		}
	}

	public void publishTasks(List<Task> tasks) {

		Object service = PlatformUI.getWorkbench().getService(IEventBroker.class);

		if (service instanceof IEventBroker) {
			((IEventBroker) service).post("com/example/eventbroker/advanced", tasks);
		}
	}

	public void publishSubmission(Submission submission) {

		Object service = PlatformUI.getWorkbench().getService(IEventBroker.class);
		if (service instanceof IEventBroker) {
			((IEventBroker) service).post("com/example/eventbroker/advanced", submission);

		}
	}

	public void publishTestResult(TestResult testResult) {

		Object service = PlatformUI.getWorkbench().getService(IEventBroker.class);
		if (service instanceof IEventBroker) {
			((IEventBroker) service).post("com/example/eventbroker/advanced", testResult);

		}
	}
}