package de.lksbhm.gdx.users;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.lksbhm.gdx.util.Instantiator;

/**
 * Responsible for loading and persisting {@link User}s and their attached data.
 * 
 *
 */
public class UserManager<UserImplementation extends User> {
	private static final String userPreferencesName = "users";
	private static final String currentUserKey = "currentUser";

	private UserImplementation currentUser;
	private final UserDataStorage userDataStorage = new UserDataStorage();
	private final Preferences userPreferences = Gdx.app
			.getPreferences(userPreferencesName);
	private final Instantiator<UserImplementation> userInstantiator;

	public UserManager(Instantiator<UserImplementation> userInstantiator) {
		this.userInstantiator = userInstantiator;
		if (userPreferences.contains(currentUserKey)) {
			int currentUserId = userPreferences.getInteger(currentUserKey);
			currentUser = loadUser(currentUserId);
		}
	}

	private UserImplementation loadUser(int id) {
		UserImplementation user = userInstantiator.instantiate();
		user.setUserId(id);
		user.callLoadAttributes(userDataStorage);
		return user;
	}

	private int generateNewUserId() {
		// TODO
		return userPreferences.getInteger("user" + (getUsersCount() - 1)) + 1;
	}

	public UserImplementation createUser() {
		UserImplementation user = userInstantiator.instantiate();
		user.setUserId(generateNewUserId());
		user.setInitialAttributeValues();
		user.callStoreAttributes(userDataStorage);
		userDataStorage.persist(user);
		return user;
	}

	public void deleteUser(UserImplementation user) {
		// TODO
	}

	public void updateUser(UserImplementation user) {
		user.callStoreAttributes(userDataStorage);
		userDataStorage.persist(user);
	}

	@SuppressWarnings("unchecked")
	private UserImplementation[] makeUserImplementationArray(int size) {
		return userInstantiator.allocateArray(size);
	}

	public UserImplementation[] listUsers() {
		int usersCount = getUsersCount();
		UserImplementation[] userList = makeUserImplementationArray(usersCount);
		UserImplementation current;
		for (int i = 0; i < usersCount; i++) {
			current = userInstantiator.instantiate();
			current.setUserId(userPreferences.getInteger("user" + i));
			userList[i] = current;
		}
		return null;
	}

	public int getUsersCount() {
		return userPreferences.getInteger("usersCount");
	}

	public UserImplementation getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserImplementation user) {
		currentUser = user;
		userPreferences.putInteger(currentUserKey, user.getUserId());
		userPreferences.flush();
	}
}
