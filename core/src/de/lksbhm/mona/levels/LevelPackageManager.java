package de.lksbhm.mona.levels;

import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.mona.Mona;

public class LevelPackageManager {
	private LevelPackageCollection internalPackages = null;
	private DailyPackagesGenerator dailyPackagesLoader = null;

	public Loadable<LevelPackageCollection> getDailyPackagesLoader() {
		if (dailyPackagesLoader == null) {
			GregorianCalendarInterface today = Mona.getGame()
					.getPlatformManager().getPlatform().getToday();
			dailyPackagesLoader = new DailyPackagesGenerator(today);
		}
		return dailyPackagesLoader;
	}

	public LevelPackageCollection getInternalPackages() {
		if (internalPackages == null) {
			internalPackages = InternalPackageLoadHelper.getInternalPackages();
		}
		return internalPackages;
	}
}
