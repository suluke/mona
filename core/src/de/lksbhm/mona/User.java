package de.lksbhm.mona;

import java.util.HashSet;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.KeyValueStore;
import de.lksbhm.mona.levels.Level;

public class User extends de.lksbhm.gdx.users.User {
	private static final String levelIdSeparator = ";";
	private static final String packageIdSeparator = ";";
	private static final String levelIdsKey = "solvedLevels";
	private static final String packageIdsKey = "solvedPackages";

	private final HashSet<String> solvedLevels = new HashSet<String>();
	private final HashSet<String> solvedPackages = new HashSet<String>();

	public int getRewardsCount() {
		return 0;
	}

	public Level getTutorialLevelToPlay() {
		return LksBhmGame.getGame(Mona.class).getLevelPackageManager()
				.getInternalPackages().getPackage(0).getLevel(0);
	}

	@Override
	protected void storeAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
		StringBuilder sb = new StringBuilder();
		for (String solved : solvedLevels) {
			sb.append(solved);
			sb.append(levelIdSeparator);
		}
		store.put(levelIdsKey, sb.toString(), this);
		sb.setLength(0);
		for (String solved : solvedPackages) {
			sb.append(solved);
			sb.append(packageIdSeparator);
		}
		store.put(packageIdsKey, sb.toString(), this);
	}

	@Override
	protected void loadAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
		String solvedLevelIds = store.get(levelIdsKey, this);
		String solvedPackageIds = store.get(packageIdsKey, this);
		String[] canonicalLevelIds = solvedLevelIds.split(levelIdSeparator);
		for (String id : canonicalLevelIds) {
			solvedLevels.add(id);
		}
		String[] packageIds = solvedPackageIds.split(packageIdSeparator);
		for (String id : packageIds) {
			solvedPackages.add(id);
		}
	}

	public void setLevelSolved(Level l) {
		solvedLevels.add(l.getCanonicalId());
		LksBhmGame.getGame(Mona.class).getUserManager().updateUser(this);
	}

	public int getRewardCount() {
		return 0;
	}

	public void addReward(int reward) {

	}

	public boolean isLevelSolved(Level l) {
		return solvedPackages.contains(l.getPackage().getPackageId())
				|| solvedLevels.contains(l.getCanonicalId());
	}
}
