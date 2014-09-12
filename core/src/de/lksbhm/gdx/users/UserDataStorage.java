package de.lksbhm.gdx.users;

import com.badlogic.gdx.Preferences;

import de.lksbhm.gdx.util.KeyValueStore;

class UserDataStorage implements KeyValueStore<User> {

	private Preferences getPreferencesForUser(User user) {
		return null;
	}

	@Override
	public String get(String key, User context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String key, String value, User context) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getFloat(String key, User context) {
		return Float.parseFloat(get(key, context));
	}

	@Override
	public double getDouble(String key, User context) {
		return Double.parseDouble(get(key, context));
	}

	@Override
	public int getInt(String key, User context) {
		return Integer.parseInt(get(key, context));
	}

	@Override
	public long getLong(String key, User context) {
		return Long.parseLong(get(key, context));
	}

	@Override
	public boolean getBoolean(String key, User context) {
		return Boolean.parseBoolean(get(key, context));
	}

	@Override
	public void put(String key, double value, User context) {
		put(key, Double.toString(value), context);
	}

	@Override
	public void put(String key, float value, User context) {
		put(key, Float.toString(value), context);
	}

	@Override
	public void put(String key, long value, User context) {
		put(key, Long.toString(value), context);
	}

	@Override
	public void put(String key, int value, User context) {
		put(key, Integer.toString(value), context);
	}

	@Override
	public void put(String key, boolean value, User context) {
		put(key, Boolean.toString(value), context);
	}
}
