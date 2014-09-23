package de.lksbhm.mona.levels;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;

public class LevelPackageManager {
	private LevelPackageCollection internalPackages = null;
	private LevelPackageCollection dailyPackages = null;

	public LevelPackageCollection getDailyPackages() {
		if (dailyPackages == null) {
			dailyPackages = DailyPackagesGenerator.getDailyPackages(LksBhmGame
					.getGame(Mona.class).getPlatformManager().getPlatform()
					.getToday());
		}
		return dailyPackages;
	}

	public LevelPackageCollection getInternalPackages() {
		if (internalPackages == null) {
			internalPackages = InternalPackageLoadHelper.getInternalPackages();
		}
		return internalPackages;
	}
}
