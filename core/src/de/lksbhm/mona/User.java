package de.lksbhm.mona;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.gdx.util.KeyValueStore;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelPackage;

public class User extends de.lksbhm.gdx.users.User {
	private static final String levelIdSeparator = ";";
	private static final String packageIdSeparator = ";";
	private static final String levelIdsKey = "solvedLevels";
	private static final String packageIdsKey = "solvedPackages";
	private static final String rewardCountKey = "rewardCount";

	private final HashSet<String> solvedLevels = new HashSet<String>();
	private final HashSet<String> solvedPackages = new HashSet<String>();
	private final HashMap<String, Integer> solvedLevelsInPackage = new HashMap<String, Integer>();

	private int rewardCount;

	@Override
	protected void storeAttributes(KeyValueStore<de.lksbhm.gdx.users.User> store) {
		store.put(rewardCountKey, rewardCount, this);

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
		rewardCount = store.getInt(rewardCountKey, this);

		String solvedPackageIds = store.get(packageIdsKey, this);
		String[] packageIds = solvedPackageIds.split(packageIdSeparator);
		for (String id : packageIds) {
			solvedPackages.add(id);
		}
		String solvedLevelIds = store.get(levelIdsKey, this);
		if (solvedLevelIds.length() != 0) {
			String[] canonicalLevelIds = solvedLevelIds.split(levelIdSeparator);
			String packageId;
			Integer solvedCount;
			for (String id : canonicalLevelIds) {
				packageId = Level.getPackageIdfromCanonicalId(id);
				if (isLevelRelevantForPersisting(packageId)) {
					solvedLevels.add(id);
					solvedCount = solvedLevelsInPackage.get(packageId);
					if (solvedCount == null) {
						solvedCount = 1;
					} else {
						solvedCount++;
					}
					solvedLevelsInPackage.put(packageId, solvedCount);
				}
			}
		}
	}

	private boolean isLevelRelevantForPersisting(String packageId) {
		boolean result = !solvedPackages.contains(packageId);
		String dailyPrefix = LksBhmGame.getGame(Mona.class).getSettings().statics
				.getDailyPackageIdPrefix();
		if (isDailyPackageId(packageId)) {
			if (!packageId.startsWith(dailyPrefix)) {
				result = false;
			}
		}
		return result;
	}

	private boolean isDailyPackageId(String packageId) {
		return packageId.length() == 8;
	}

	public void setLevelSolved(Level l) {
		if (solvedLevels.contains(l)) {
			return;
		} else {
			String packId = l.getPackage().getPackageId();
			Integer solvedCount = solvedLevelsInPackage.get(packId);
			if (solvedCount == null) {
				solvedCount = 0;
			}
			if (solvedCount == l.getPackage().getSize() - 1) {
				String packageId = l.getPackage().getPackageId()
						+ Level.packageIdSeparator;
				Iterator<String> it = solvedLevels.iterator();
				String id;
				while (it.hasNext()) {
					id = it.next();
					if (!id.startsWith(packageId)) {
						it.remove();
					}
				}
				setPackageSolved(l.getPackage());
			} else {
				// another single level solved
				solvedLevels.add(l.getCanonicalId());
				solvedLevelsInPackage.put(packId, solvedCount + 1);
			}
			getUserManager().updateUser(this);
		}
	}

	private void setPackageSolved(LevelPackage pack) {
		// whole package solved
		String packId = pack.getPackageId();
		solvedPackages.add(packId);
		solvedLevelsInPackage.remove(packId);
		pack.notifySolved();
	}

	public int getRewardCount() {
		return rewardCount;
	}

	private UserManager<User> getUserManager() {
		return LksBhmGame.getGame(Mona.class).getUserManager();
	}

	public void addReward(int reward) {
		rewardCount += reward;
		getUserManager().updateUser(this);
	}

	public boolean isLevelSolved(Level l) {
		return solvedPackages.contains(l.getPackage().getPackageId())
				|| solvedLevels.contains(l.getCanonicalId());
	}

	public boolean isPackageSolved(LevelPackage pack) {
		return solvedPackages.contains(pack.getPackageId());
	}
}
