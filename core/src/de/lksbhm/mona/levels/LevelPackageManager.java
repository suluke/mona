package de.lksbhm.mona.levels;

import java.util.Calendar;

public class LevelPackageManager {
	private final Calendar today;
	private LevelPackageCollection internalPackages = null;

	public LevelPackageManager() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
				now.get(Calendar.DATE));
		today = now;
	}

	public LevelPackageCollection getDailyPackages() {
		return DailyPackagesGenerator.getDailyPackages(today);
	}

	public LevelPackageCollection getInternalPackages() {
		if (internalPackages == null) {
			internalPackages = InternalPackageLoadHelper.getInternalPackages();
		}
		return internalPackages;
	}
}
