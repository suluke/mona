package de.lksbhm.mona;

import java.util.HashMap;

import de.lksbhm.gdx.util.KeyValueStore;

public class User extends de.lksbhm.gdx.users.User {

	private final HashMap<String, Boolean> solvedPuzzles = new HashMap<String, Boolean>();
	private final HashMap<String, Boolean> solvedPackages = new HashMap<String, Boolean>();

	public int getRewardsCount() {
		return 0;
	}

	public boolean hasPlayedTutorials() {
		return false;
	}

	@Override
	protected void storeAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
	}

	@Override
	protected void loadAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
	}
}
