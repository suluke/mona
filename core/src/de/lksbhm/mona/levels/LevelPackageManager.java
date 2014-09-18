package de.lksbhm.mona.levels;

import java.util.Calendar;

public class LevelPackageManager {
	private final Calendar today;
	private LevelPackageCollection internalPackages = null;
	private LevelPackageCollection dailyPackages = null;

	public LevelPackageManager() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);
		now.setTimeInMillis(0);
		now.set(year, month, day);
		today = now;
	}

	public LevelPackageCollection getDailyPackages() {
		if (dailyPackages == null) {
			dailyPackages = DailyPackagesGenerator.getDailyPackages(today);
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
