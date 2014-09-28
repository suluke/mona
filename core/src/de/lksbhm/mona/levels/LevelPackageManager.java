package de.lksbhm.mona.levels;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.mona.Mona;

public class LevelPackageManager {
	private LevelPackageCollection internalPackages = null;
	private LevelPackageCollection dailyPackages = null;

	public LevelPackageCollection getDailyPackages() {
		if (dailyPackages == null) {
			GregorianCalendarInterface today = LksBhmGame.getGame(Mona.class)
					.getPlatformManager().getPlatform().getToday();
			DailyPackagesGenerator generator = new DailyPackagesGenerator(today);
			generator.finish();
			dailyPackages = generator.getPackages();
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
