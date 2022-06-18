package de.lmu.gateplugin.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.fasterxml.jackson.core.type.TypeReference;

import de.lmu.gateplugin.model.Student;
import de.lmu.gateplugin.model.Submission;
import de.lmu.gateplugin.model.Task;
import de.lmu.gateplugin.model.Test;
import de.lmu.gateplugin.model.TestResult;
import de.lmu.gateplugin.oauth.AuthConfig;
import de.lmu.gateplugin.oauth.Token;
import de.lmu.gateplugin.oauth.UserInfo;
import de.lmu.gateplugin.ui.Activator;
import de.lmu.gateplugin.ui.pages.AuthorizePage;
import de.lmu.gateplugin.ui.pages.LoginPage;
import de.lmu.gateplugin.ui.pages.Template;

public class ApiRequestJob {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();
	private static final GatePreferenceStore GATE_PREFERENCE_STORE = GatePreferenceStore.getGatePreferenceStore();
	private static final RestClient restClient = RestClient.getInstance();

	private static ApiRequestJob instance;

	private static final String AUTHORIZE_PATH = "login/authorize";
	private static final String TOKEN_PATH = "login/token";
	private static final String USER_INFO_PATH = "login/userinfo";

	private static final String USER_EMAIL_PATH = "users/email/";
	private static final String AVAILABLE_TASKS_PATH = "tasks/user/";
	private static final String USER_SUBMISSION_FOR_TASK_PATH = "submissions/task/";
	private static final String DOWNLOAD_TASK_PATH = "tasks/";
	private static final String SUBMIT_SOLUTION_PATH = "submissions/task/";
	private static final String EXECUTE_TEST_PATH = "tests/";

	public static ApiRequestJob getInstance() {
		if (instance == null) {
			instance = new ApiRequestJob();
		}
		return instance;
	}

	public void authorizeDeviceJob(Composite parent) {

		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			public void run() {

				try {
					String requestPath = AUTHORIZE_PATH;
					Response response = restClient.authorizeRequest(requestPath);
					AuthConfig authConfig = restClient.getObjectMapper().readValue(response.readEntity(String.class),
							AuthConfig.class);
					PLUGIN_INSTANCE.getLogger().info("Authorizing device");
					LoginPage.navigateToAuthorizationPage(authConfig, parent);

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
				}
			}
		});
	}

	public Job getAccessTokenAndUserInfoJob(AuthConfig authConfig, Composite parent, Template template) {

		return new Job("Poll and get access token") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				Token token = null;
				int pollingDelay = authConfig.getInterval();

				try {
					String requestPath = TOKEN_PATH;
					Response response = restClient.tokenRequest(requestPath, authConfig);

					token = restClient.getObjectMapper().readValue(response.readEntity(String.class), Token.class);

					GATE_PREFERENCE_STORE.setAccessToken(token.getAccessToken());
					GATE_PREFERENCE_STORE.setRefreshToken(token.getRefreshToken());
					// TODO set access token expires in plus Instant.now
					// TODO set refresh token expires in plus Instant.now

					Job userInfoJob = getUserInfoJob(parent, template);
					userInfoJob.setSystem(true);
					userInfoJob.schedule();

				} catch (IOException e1) {
					cancel();
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {

					String tokenError = e2.getTokenError().getError();

					switch (tokenError) {

					case "slow_down":
						pollingDelay += 5;
						break;

					case "authorization_pending":
						// do nothing
						break;

					default:
						cancel();
						return Status.error(tokenError);
					}

					PLUGIN_INSTANCE.getLogger().info(tokenError);

				} catch (ProcessingException e3) {
					cancel();
					String message = "Connection refused, please make sure the server is running.";
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);
				}

				if (token != null) {

					cancel();
					return Status.OK_STATUS;

				} else {

					schedule(pollingDelay * 1000);
					return Status.info("Polling again in " + pollingDelay * 1000 + "ms");
				}
			}
		};
	}

	private Job getUserInfoJob(Composite parent, Template template) {

		return new Job("Get currently logged in user") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = USER_INFO_PATH;
					Response response = restClient.getRequest(requestPath);

					UserInfo userInfo = restClient.getObjectMapper().readValue(response.readEntity(String.class),
							UserInfo.class);

					GATE_PREFERENCE_STORE.setEmail(userInfo.getEmail());
					GATE_PREFERENCE_STORE.setName(userInfo.getName());
					GATE_PREFERENCE_STORE.setUsername(userInfo.getPreferredUsername());

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showFailedAuthorizationMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo(),
							parent, template);
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showFailedAuthorizationMessageDialog(message, "502 Connection Refused", parent, template);
					PLUGIN_INSTANCE.getLogger().info(message);

				} catch (TokenExpiredException e4) {
					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), getUserInfoJob(parent, template));
				}

				return Status.OK_STATUS;
			}
		};
	}

	public void getUserByEmailJob(Composite parent, Template template) {

		try {
			String requestPath = USER_EMAIL_PATH + GATE_PREFERENCE_STORE.getEmail();

			Response response = restClient.getRequest(requestPath);

			Student student = restClient.getObjectMapper().readValue(response.readEntity(String.class), Student.class);
			GATE_PREFERENCE_STORE.setUserId(student.getUid());

		} catch (IOException e1) {
			PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());

		} catch (RestClientException e2) {
			Util.showFailedAuthorizationMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo(), parent,
					template);
			PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());

		} catch (ProcessingException e3) {
			String message = "Connection refused, please make sure the server is running.";
			Util.showFailedAuthorizationMessageDialog(message, "502 Connection Refused", parent, template);
			PLUGIN_INSTANCE.getLogger().info(message);

		} catch (TokenExpiredException e4) {

			Job refreshToken = refreshTokenJob();
			refreshToken.setSystem(true);
			refreshToken.schedule();

			refreshToken.addJobChangeListener(new JobChangeAdapter() {
				public void done(IJobChangeEvent event) {
					if (event.getResult().isOK()) {
						getUserByEmailJob(parent, template);
					}
				}
			});

			PLUGIN_INSTANCE.getLogger().info("");
		}
	}

	public Job getUserAvailableTasksJob() {

		return new Job("Get available tasks for user") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = AVAILABLE_TASKS_PATH + GATE_PREFERENCE_STORE.getUserId();
					Response response = restClient.getRequest(requestPath);

					List<Task> tasks = restClient.getObjectMapper().readValue(response.readEntity(String.class),
							new TypeReference<List<Task>>() {
							});

					PLUGIN_INSTANCE.getLogger().info("Gotten tasks");
					PLUGIN_INSTANCE.publishTasks(tasks);
					return Status.OK_STATUS;

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.info(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);

				} catch (TokenExpiredException e4) {

					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), getUserAvailableTasksJob());
					return Status.info("");
				}
			}
		};
	}

	public Job getUserSubmissionForTaskJob(Task task) {

		return new Job("Get submission user has made for task") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = USER_SUBMISSION_FOR_TASK_PATH + task.getTaskid() + "/user/"
							+ GATE_PREFERENCE_STORE.getUserId();
					Response response = restClient.getRequest(requestPath);

					String responseString = response.readEntity(String.class);

					Submission submission = null;

					if (!responseString.equals("{}")) {
						submission = restClient.getObjectMapper().readValue(responseString, Submission.class);
					}

					PLUGIN_INSTANCE.publishSubmission(submission);
					PLUGIN_INSTANCE.getLogger().info("Gotten user submission for task");
					return Status.OK_STATUS;

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.error(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);

				} catch (TokenExpiredException e4) {

					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), getUserSubmissionForTaskJob(task));
					return Status.info("");
				}
			}
		};
	}

	public Job executeTestJob(Test test) {

		return new Job("Execute test") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = EXECUTE_TEST_PATH + test.getId() + "/user/"
							+ GATE_PREFERENCE_STORE.getUserId();
					Response response = restClient.getAsyncRequest(requestPath);

					TestResult testResult = restClient.getObjectMapper().readValue(response.readEntity(String.class),
							TestResult.class);

					PLUGIN_INSTANCE.getLogger().info("Gotten test result");
					PLUGIN_INSTANCE.publishTestResult(testResult);
					return Status.OK_STATUS;

				} catch (IOException | InterruptedException | ExecutionException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.error(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);

				} catch (TokenExpiredException e4) {
					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), executeTestJob(test));
					return Status.info("");
				}
			}
		};
	}

	public Job createJavaProjectJob(Task task) {

		return new Job("Create Java project for task") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = DOWNLOAD_TASK_PATH + task.getTaskid();
					Response response = restClient.downloadFile(requestPath);

					InputStream inputStream = response.readEntity(InputStream.class);

					try {
						Util.createJavaProject(task, inputStream, monitor);
					} catch (CoreException e) {
						PLUGIN_INSTANCE.getLogger().warn(e.getMessage());
						e.printStackTrace();
					}

					return Status.OK_STATUS;

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.error(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = "Connection refused, please make sure the server is running.";
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);

				} catch (TokenExpiredException e4) {
					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), createJavaProjectJob(task));
					return Status.info("");
				}
			}
		};
	}

	public Job submitSolution(File file, Task task) {

		return new Job("Submit solution for task") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					String requestPath = SUBMIT_SOLUTION_PATH + task.getTaskid() + "/user/"
							+ GATE_PREFERENCE_STORE.getUserId();

					restClient.uploadFile(file, requestPath);

					PLUGIN_INSTANCE.getLogger().info("Submitting task to GATE");
					return Status.OK_STATUS;

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.error(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = e3.getMessage();
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);

				} catch (TokenExpiredException e4) {
					refreshTokenAndPerformOutstandingJob(refreshTokenJob(), submitSolution(file, task));
					return Status.info("");
				}
			}
		};
	}

	private Job refreshTokenJob() {

		return new Job("Refresh token job") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {

					PLUGIN_INSTANCE.getLogger().info("Access token expired, now refreshing...");
					Response response = restClient.refreshTokenRequest();

					Token token = restClient.getObjectMapper().readValue(response.readEntity(String.class),
							Token.class);

					GATE_PREFERENCE_STORE.setAccessToken(token.getAccessToken());
					GATE_PREFERENCE_STORE.setRefreshToken(token.getRefreshToken());
					PLUGIN_INSTANCE.getLogger().info("Refreshed tokens");

					return Status.OK_STATUS;

				} catch (IOException e1) {
					PLUGIN_INSTANCE.getLogger().warn(e1.getMessage());
					return Status.error(e1.getMessage());

				} catch (RestClientException e2) {
					Util.showInfoMessageDialog(e2.getErrorMessage().getMessage(), e2.getStatusInfo());
					PLUGIN_INSTANCE.getLogger().info(e2.getErrorMessage().getMessage());
					return Status.error(e2.getErrorMessage().getMessage());

				} catch (ProcessingException e3) {
					String message = e3.getMessage();
					Util.showInfoMessageDialog(message, "502 Connection Refused");
					PLUGIN_INSTANCE.getLogger().info(message);
					return Status.error(message);
				}
			}
		};
	}

	public void refreshTokenAndPerformOutstandingJob(Job refreshTokenJob, Job outstandingJob) {

		refreshTokenJob.setSystem(true);
		refreshTokenJob.schedule();

		refreshTokenJob.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {

					outstandingJob.setSystem(true);
					outstandingJob.schedule();
				} else {
					AuthorizePage.navigateToLoginPage(PLUGIN_INSTANCE.getParentComposite(),
							PLUGIN_INSTANCE.getTemplate());
				}
			}
		});
	}

}