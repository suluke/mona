package de.lksbhm.gdx.users;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.KeyValueStore;

public abstract class User {

	private final int userId;
	private String displayName;

	public User(int id) {
		userId = id;
	}

	int getUserId() {
		return userId;
	}

	public void setName(String name) {
		this.displayName = name;
	}

	public String getName() {
		return displayName;
	}

	void loadAttributes() {
		loadAttributes(LksBhmGame.getGame().getUserManager());
	}

	protected void loadAttributes(KeyValueStore<User> store) {
		displayName = store.get("name", this);
	}

	void storeAttributes() {
		storeAttributes(LksBhmGame.getGame().getUserManager());
	}

	protected void storeAttributes(KeyValueStore<User> store) {
		store.put("name", displayName, this);
	}
}
