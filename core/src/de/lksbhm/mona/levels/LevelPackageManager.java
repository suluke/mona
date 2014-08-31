package de.lksbhm.mona.levels;

import java.util.Calendar;

public class LevelPackageManager {
	private final Calendar today;

	public LevelPackageManager() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
				now.get(Calendar.DATE));
		today = now;
	}

	public int getNumberOfDailyPackages() {
		return DailyPackagesGenerator.getNumberOfPackages(today);
	}

	public LevelPackageCollection getDailyPackages() {
		return DailyPackagesGenerator.getDailyPackages(today);
	}

	public LevelPackageCollection getInternalPackages() {
		LevelPackageCollection collection = new LevelPackageCollection(1);
		collection.setPackage(0, new InternalPackage());
		return collection;
	}

	public int getInternalPackagesCount() {
		return 0;
	}
}
