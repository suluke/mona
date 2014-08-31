package de.lksbhm.gdx;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public abstract class Settings {
	private final HashSet<String> userNames = new HashSet<String>();

	public void putString(String key, String value, String userName) {
		userNames.add(userName);
		Preferences prefs = Gdx.app.getPreferences(userName);
		prefs.putString(key, value);
	}

	public String getString(String key, String userName) {
		Preferences prefs = Gdx.app.getPreferences(userName);
		if (prefs.contains(key)) {
			return prefs.getString(key);
		} else {
			return null;
		}
	}

	public boolean getBoolean(String key, String userName) {
		Preferences prefs = Gdx.app.getPreferences(userName);
		if (prefs.contains(key)) {
			return prefs.getBoolean(key);
		} else {
			return false;
		}
	}

	public void putBoolean(String key, boolean value, String userName) {
		userNames.add(userName);
		Preferences prefs = Gdx.app.getPreferences(userName);
		prefs.putBoolean(key, value);
	}

	public void flushSettings() {
		for (String userName : userNames) {
			Gdx.app.getPreferences(userName).flush();
		}
	}
}
