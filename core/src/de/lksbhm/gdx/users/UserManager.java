package de.lksbhm.gdx.users;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.lksbhm.gdx.LksBhmGame;

/**
 * Responsible for loading and persisting {@link User}s and their attached data.
 * 
 *
 */
public class UserManager {
	private static final String userPreferencesName = "users";

	private User currentUser;
	private final UserDataStorage userDataStorage = new UserDataStorage();
	private final Preferences userPreferences = Gdx.app
			.getPreferences(userPreferencesName);

	public UserManager() {

	}

	private int generateNewUserId() {
		return 0;
	}

	public User createUser() {
		return null;
	}

	public void deleteUser(User user) {

	}

	public void updateUser(User user) {
		user.storeAttributes(userDataStorage);
	}

	public User[] listUsers() {
		LksBhmGame game = LksBhmGame.getGame();
		int usersCount = getUsersCount();
		User[] userList = new User[usersCount];
		User current;
		for (int i = 0; i < usersCount; i++) {
			current = game.instantiateUserImplementation();
			current.setUserId(userPreferences.getInteger("user" + i));
			userList[i] = current;
		}
		return null;
	}

	public int getUsersCount() {
		return userPreferences.getInteger("usersCount");
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User user) {
		currentUser = user;
		userPreferences.putInteger("currentUser", user.getUserId());
		userPreferences.flush();
	}
}
