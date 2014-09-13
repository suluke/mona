package de.lksbhm.gdx.users;

import java.lang.reflect.Array;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * Responsible for loading and persisting {@link User}s and their attached data.
 * 
 *
 */
public class UserManager<UserImplementation extends User> {
	private static final String userPreferencesName = "users";

	private UserImplementation currentUser;
	private final UserDataStorage userDataStorage = new UserDataStorage();
	private final Preferences userPreferences = Gdx.app
			.getPreferences(userPreferencesName);
	private final Class<UserImplementation> userImplementationClass;
	private final Constructor userImplementationConstructor;

	public UserManager(Class<UserImplementation> userImplementationClass) {
		this.userImplementationClass = userImplementationClass;
		try {
			userImplementationConstructor = ClassReflection.getConstructor(
					userImplementationClass, new Class<?>[0]);
		} catch (ReflectionException e) {
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unchecked")
	private UserImplementation instantiateUser() {
		try {
			return (UserImplementation) userImplementationConstructor
					.newInstance(new Object[0]);
		} catch (ReflectionException e) {
			throw new RuntimeException();
		}
	}

	private int generateNewUserId() {
		// TODO
		return 0;
	}

	public UserImplementation createUser() {
		UserImplementation user = instantiateUser();
		user.setUserId(generateNewUserId());
		user.setInitialAttributeValues();
		return user;
	}

	public void deleteUser(UserImplementation user) {

	}

	public void updateUser(UserImplementation user) {
		user.storeAttributes(userDataStorage);
	}

	@SuppressWarnings("unchecked")
	private UserImplementation[] makeUserImplementationArray(int size) {
		return (UserImplementation[]) Array.newInstance(
				userImplementationClass, size);
	}

	public UserImplementation[] listUsers() {
		int usersCount = getUsersCount();
		UserImplementation[] userList = makeUserImplementationArray(usersCount);
		UserImplementation current;
		for (int i = 0; i < usersCount; i++) {
			current = instantiateUser();
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
		userPreferences.putInteger("currentUser", user.getUserId());
		userPreferences.flush();
	}
}
