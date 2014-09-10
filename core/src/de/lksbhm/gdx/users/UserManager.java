package de.lksbhm.gdx.users;

import de.lksbhm.gdx.util.KeyValueStore;

/**
 * Responsible for loading and persisting {@link User}s and their attached data.
 * 
 *
 */
public class UserManager implements KeyValueStore<User> {
	public User createUser(String name) {
		return null;
	}

	public void deleteUser(User user) {

	}

	public void updateUser(User user) {

	}

	public User getLastUser() {
		return null;
	}

	public String[] listUserNames() {
		return null;
	}

	@Override
	public String get(String key, User user) {
		return null;
	}

	@Override
	public void put(String key, String value, User user) {

	}

	public static boolean isUserNameValid(String name) {
		if ("default".equalsIgnoreCase(name)) {
			return false;
		} else if ("preferences".equalsIgnoreCase(name)) {
			return false;
		} else if ("settings".equalsIgnoreCase(name)) {
			return false;
		}

		return true;
	}
}
