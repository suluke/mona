package de.lksbhm.gdx.users;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.lksbhm.gdx.util.KeyValueStore;

class UserDataStorage implements KeyValueStore<User> {

	private Preferences getPreferencesForUser(User user) {
		return Gdx.app.getPreferences("user" + user.getUserId());
	}

	@Override
	public String get(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getString(key);
	}

	@Override
	public void put(String key, String value, User user) {
		Preferences prefs = getPreferencesForUser(user);
		prefs.putString(key, value);
	}

	@Override
	public float getFloat(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getFloat(key);
	}

	@Override
	public double getDouble(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getFloat(key);
	}

	@Override
	public int getInt(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getInteger(key);
	}

	@Override
	public long getLong(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getLong(key);
	}

	@Override
	public boolean getBoolean(String key, User user) {
		Preferences prefs = getPreferencesForUser(user);
		return prefs.getBoolean(key);
	}

	@Override
	public void put(String key, double value, User user) {
		put(key, Double.toString(value), user);
	}

	@Override
	public void put(String key, float value, User user) {
		Preferences prefs = getPreferencesForUser(user);
		prefs.putFloat(key, value);
	}

	@Override
	public void put(String key, long value, User user) {
		Preferences prefs = getPreferencesForUser(user);
		prefs.putLong(key, value);
	}

	@Override
	public void put(String key, int value, User user) {
		Preferences prefs = getPreferencesForUser(user);
		prefs.putInteger(key, value);
	}

	@Override
	public void put(String key, boolean value, User user) {
		Preferences prefs = getPreferencesForUser(user);
		prefs.putBoolean(key, value);
	}

	@Override
	public void persist(User user) {
		System.out.println("persist");
		getPreferencesForUser(user).flush();
	}
}
