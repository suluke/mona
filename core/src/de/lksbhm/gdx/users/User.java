package de.lksbhm.gdx.users;

import com.badlogic.gdx.utils.TimeUtils;

import de.lksbhm.gdx.util.KeyValueStore;

public abstract class User {

	private String displayName;
	private long creationTime = TimeUtils.millis();
	private int userId;

	int getUserId() {
		return userId;
	}

	void setUserId(int userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.displayName = name;
	}

	public String getName() {
		return displayName;
	}

	/*
	 * To allow sorting by time of creation
	 */
	public long getCreationTime() {
		return creationTime;
	}

	final void callLoadAttributes(KeyValueStore<User> store) {
		displayName = store.get("name", this);
		creationTime = store.getLong("creationTime", this);
		loadAttributes(store);
	}

	protected void loadAttributes(KeyValueStore<User> store) {

	}

	final void callStoreAttributes(KeyValueStore<User> store) {
		store.put("name", displayName, this);
		store.put("creationTime", creationTime, this);
		storeAttributes(store);
	}

	protected void storeAttributes(KeyValueStore<User> store) {

	}
}
