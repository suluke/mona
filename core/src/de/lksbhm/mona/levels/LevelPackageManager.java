package de.lksbhm.mona.levels;

import java.util.Calendar;

public class LevelPackageManager {
	private final Calendar today;

	private LevelPackageManager() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
				now.get(Calendar.DATE));
		today = now;
	}

	public int getNumberOfCampaignPackages() {
		return 0;
	}

	public int getNumberOfDailyPackages() {
		return DailyPackagesGenerator.getNumberOfPackages(today);
	}

	public LevelPackageCollection getDailyPackages() {
		return DailyPackagesGenerator.getDailyPackages(today);
	}

	public LevelPackageCollection getCampaignPackages() {
		return null;
	}
}
