package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.levels.Difficulty;

public final class StaticSettings {
	private String idPrefix;

	StaticSettings() {

	}

	public int getLevelsPerPackage() {
		return 20;
	}

	public int getMaximumNumberOfUsers() {
		return 1;
	}

	public int getMinPuzzleWidthForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 2;
		case EASY:
			return 4;
		case MEDIUM:
			return 6;
		case HARD:
			return 8;
		case VERY_HARD:
			return 10;
		default:
			throw new RuntimeException();
		}
	}

	public int getMaxPuzzleWidthForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 3;
		case EASY:
			return 5;
		case MEDIUM:
			return 7;
		case HARD:
			return 9;
		case VERY_HARD:
			return 11;
		default:
			throw new RuntimeException();
		}
	}

	public int getMinPuzzleHeightForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 3;
		case EASY:
			return 5;
		case MEDIUM:
			return 7;
		case HARD:
			return 9;
		case VERY_HARD:
			return 11;
		default:
			throw new RuntimeException();
		}
	}

	public int getMaxPuzzleHeightForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 4;
		case EASY:
			return 6;
		case MEDIUM:
			return 8;
		case HARD:
			return 10;
		case VERY_HARD:
			return 12;
		default:
			throw new RuntimeException();
		}
	}

	public int getMinimumNumberOfDailyPackages() {
		return 1;
	}

	public int getMaximumNumberOfDailyPackages() {
		return 4;
	}

	public String getDailyPackageIdPrefix() {
		if (idPrefix == null) {
			MonaPlatform platform = LksBhmGame.getGame(Mona.class)
					.getPlatformManager().getPlatform();
			idPrefix = platform.formatCalendarLocalized(platform.getToday(),
					"yyyyMMdd");
		}
		return idPrefix;
	}

	public int getRewardsNeededForDailies() {
		return 10;
	}

	public int getRewardsNeededForRandom() {
		return 20;
	}
}
