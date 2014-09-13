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
		return Float.parseFloat(get(key, user));
	}

	@Override
	public double getDouble(String key, User user) {
		return Double.parseDouble(get(key, user));
	}

	@Override
	public int getInt(String key, User user) {
		return Integer.parseInt(get(key, user));
	}

	@Override
	public long getLong(String key, User user) {
		return Long.parseLong(get(key, user));
	}

	@Override
	public boolean getBoolean(String key, User user) {
		return Boolean.parseBoolean(get(key, user));
	}

	@Override
	public void put(String key, double value, User user) {
		put(key, Double.toString(value), user);
	}

	@Override
	public void put(String key, float value, User user) {
		put(key, Float.toString(value), user);
	}

	@Override
	public void put(String key, long value, User user) {
		put(key, Long.toString(value), user);
	}

	@Override
	public void put(String key, int value, User user) {
		put(key, Integer.toString(value), user);
	}

	@Override
	public void put(String key, boolean value, User user) {
		put(key, Boolean.toString(value), user);
	}

	@Override
	public void persist(User user) {
		getPreferencesForUser(user).flush();
	}
}
