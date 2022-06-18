package de.lmu.gateplugin.util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lmu.gateplugin.model.ErrorMessage;
import de.lmu.gateplugin.model.multipart.FilePart;
import de.lmu.gateplugin.model.multipart.MultiPartMessage;
import de.lmu.gateplugin.model.multipart.MultipartMessageBodyWriter;
import de.lmu.gateplugin.oauth.AuthConfig;
import de.lmu.gateplugin.oauth.TokenError;

public class RestClient {

	private static final String API_PATH = "http://localhost:8080/si_war/api/";
	private static final String REFRESH_TOKEN_PATH = "login/refresh";

	private static RestClient restClientInstance;

	private Client client;
	private WebTarget webTarget;
	private ObjectMapper objectMapper;

	public RestClient() {
		client = ClientBuilder.newClient();
		webTarget = client.target(API_PATH);
		objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	}

	public static RestClient getInstance() {
		if (restClientInstance == null) {
			restClientInstance = new RestClient();
		}
		return restClientInstance;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public String getBearerToken() {
		return "Bearer " + GatePreferenceStore.getGatePreferenceStore().getAccessToken();
	}

	public Response authorizeRequest(String requestPath) throws IOException, RestClientException, ProcessingException {

		WebTarget requestTarget = webTarget.path(requestPath);

		String body = "{\"clientId\": \"gate-ws-device-code\", " + "\"scope\":\"openid\"}";
		Response response = requestTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);
			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());
		}

		return response;
	}

	public Response tokenRequest(String requestPath, AuthConfig authConfig)
			throws IOException, RestClientException, ProcessingException {

		WebTarget requestTarget = webTarget.path(requestPath);

		String body = "{\"clientId\": \"gate-ws-device-code\", " + "\"deviceCode\": \"" + authConfig.getDeviceCode()
				+ "\"}";
		Response response = requestTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			TokenError tokenError = objectMapper.readValue(response.readEntity(String.class), TokenError.class);
			throw new RestClientException(tokenError, response.getStatus() + " " + response.getStatusInfo().toString());
		}

		return response;
	}

	public Response getRequest(String requestPath)
			throws IOException, RestClientException, ProcessingException, TokenExpiredException {

		WebTarget requestTarget = webTarget.path(requestPath);

		Response response = requestTarget.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getBearerToken()).get(Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {

				if (errorMessage.getMessage().contains("The Token has expired")) {
					throw new TokenExpiredException();
				}
			}

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());

		}

		return response;
	}

	public Response getAsyncRequest(String requestPath) throws IOException, RestClientException, ProcessingException,
			InterruptedException, ExecutionException, TokenExpiredException {

		WebTarget requestTarget = webTarget.path(requestPath);

		Invocation.Builder requestBuilder = requestTarget.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getBearerToken());
		AsyncInvoker asyncInvoker = requestBuilder.async();

		Future<Response> futureResponse = asyncInvoker.get();
		Response response = futureResponse.get();

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {

				if (errorMessage.getMessage().contains("The Token has expired")) {
					throw new TokenExpiredException();
				}
			}

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());

		}

		return response;
	}

	public Response postRequest(String requestPath, String body)
			throws IOException, RestClientException, ProcessingException, TokenExpiredException {

		WebTarget requestTarget = webTarget.path(requestPath);

		Response response = requestTarget.request().header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.post(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {

				if (errorMessage.getMessage().contains("The Token has expired")) {
					throw new TokenExpiredException();
				}
			}

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());

		}

		return response;
	}

	public Response downloadFile(String requestPath)
			throws IOException, RestClientException, ProcessingException, TokenExpiredException {

		WebTarget requestTarget = webTarget.path(requestPath);

		Response response = requestTarget.request(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.AUTHORIZATION, getBearerToken()).get();

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {

				if (errorMessage.getMessage().contains("The Token has expired")) {
					throw new TokenExpiredException();
				}
			}

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());

		}

		return response;
	}

	public void uploadFile(File file, String requestPath)
			throws IOException, RestClientException, ProcessingException, TokenExpiredException {

		Client client = ClientBuilder.newBuilder().register(MultipartMessageBodyWriter.class).build();

		WebTarget requestTarget = client.target(API_PATH).path(requestPath);

		MultiPartMessage multiPartMessage = new MultiPartMessage();
		multiPartMessage.addPart(new FilePart("file", file));

		Response response = requestTarget.request().header(HttpHeaders.AUTHORIZATION, getBearerToken())
				.post(Entity.entity(multiPartMessage, MediaType.MULTIPART_FORM_DATA), Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {

				if (errorMessage.getMessage().contains("The Token has expired")) {
					throw new TokenExpiredException();
				}
			}

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());

		}
	}

	public Response refreshTokenRequest() throws IOException, RestClientException, ProcessingException {

		WebTarget requestTarget = webTarget.path(REFRESH_TOKEN_PATH);

		String body = "{\"clientId\": \"gate-ws-device-code\", " + "\"grantType\": \"refresh_token\", "
				+ "\"refreshToken\": \"" + GatePreferenceStore.getGatePreferenceStore().getRefreshToken() + "\"}";

		Response response = requestTarget.request().post(Entity.entity(body, MediaType.APPLICATION_JSON),
				Response.class);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {

			ErrorMessage errorMessage = objectMapper.readValue(response.readEntity(String.class), ErrorMessage.class);

			throw new RestClientException(errorMessage,
					response.getStatus() + " " + response.getStatusInfo().toString());
		}

		return response;
	}

}