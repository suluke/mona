package de.lksbhm.mona;

import java.util.HashMap;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.KeyValueStore;
import de.lksbhm.mona.levels.Level;

public class User extends de.lksbhm.gdx.users.User {

	private final HashMap<String, Boolean> solvedPuzzles = new HashMap<String, Boolean>();
	private final HashMap<String, Boolean> solvedPackages = new HashMap<String, Boolean>();

	public int getRewardsCount() {
		return 0;
	}

	public Level getTutorialLevelToPlay() {
		return LksBhmGame.getGame(Mona.class).getLevelPackageManager()
				.getInternalPackages().getPackage(0).getLevel(0);
	}

	@Override
	protected void storeAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
	}

	@Override
	protected void loadAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
	}

	public void setLevelSolved(Level l, boolean b) {
	}

	public boolean isLevelSolved(Level l) {
		return false;
	}
}
