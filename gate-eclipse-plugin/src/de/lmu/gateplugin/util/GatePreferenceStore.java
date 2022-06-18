package de.lmu.gateplugin.util;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.lmu.gateplugin.ui.Activator;

public class GatePreferenceStore {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = "refreshToken";

	public static final String UID = "userId";
	public static final String USERNAME = "username";
	public static final String NAME = "name";
	public static final String EMAIL = "email";

	public static Preferences gatePrefStore;
	public static GatePreferenceStore instance;

	public GatePreferenceStore(Preferences preference) {
		gatePrefStore = preference;
	}

	public static GatePreferenceStore getGatePreferenceStore() {
		if (instance == null) {
			instance = new GatePreferenceStore(ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID));

		}
		return instance;
	}

	public void setAccessToken(String accessToken) {

		gatePrefStore.put(ACCESS_TOKEN, accessToken);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save access token");
			e.printStackTrace();
		}
	}

	public String getAccessToken() {
		return gatePrefStore.get(ACCESS_TOKEN, null);
	}

	public void setRefreshToken(String refreshToken) {

		gatePrefStore.put(REFRESH_TOKEN, refreshToken);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save refresh token");
			e.printStackTrace();
		}
	}

	public String getRefreshToken() {
		return gatePrefStore.get(REFRESH_TOKEN, null);
	}

	public void setUserId(int userId) {

		gatePrefStore.putInt(UID, userId);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save userId");
			e.printStackTrace();
		}
	}

	public int getUserId() {
		return gatePrefStore.getInt(UID, 0);
	}

	public void setUsername(String username) {

		gatePrefStore.put(USERNAME, username);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save username");
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return gatePrefStore.get(USERNAME, null);
	}

	public void setName(String name) {

		gatePrefStore.put(NAME, name);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save name");
			e.printStackTrace();
		}
	}

	public String getName() {
		return gatePrefStore.get(NAME, null);
	}

	public void setEmail(String email) {

		gatePrefStore.put(EMAIL, email);
		try {
			gatePrefStore.flush();
		} catch (BackingStoreException e) {
			PLUGIN_INSTANCE.getLogger().warn("Could'nt save email");
			e.printStackTrace();
		}
	}

	public String getEmail() {
		return gatePrefStore.get(EMAIL, null);
	}

}