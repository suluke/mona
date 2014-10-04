package de.lksbhm.mona.levels;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.QualityPuzzleGenerator;

class DailyPackagesGenerator implements Loadable<LevelPackageCollection> {
	private final Random random = new RandomXS128();
	private final Difficulty[] difficulties;
	private final LevelPackageCollection collection;
	private final String dayName;
	private final int packageSize = LksBhmGame.getGame(Mona.class)
			.getSettings().statics.getLevelsPerPackage();
	private final String dailyPackageIdPrefix = LksBhmGame.getGame(Mona.class)
			.getSettings().statics.getDailyPackageIdPrefix();
	private final long millis;

	private float progress = 0;
	private int currentLevelIndex = 0;
	private DailyPackage pack;

	DailyPackagesGenerator(GregorianCalendarInterface date) {
		difficulties = getDifficulties(date, random);
		collection = new LevelPackageCollection(difficulties.length);
		dayName = shortDayNameFromIndex(date.getDayOfWeek());
		millis = date.getTimeInMillis();
	}

	@Override
	public boolean update() {
		if (progress == 1) {
			return true;
		}
		int packageIndex = currentLevelIndex / packageSize;
		int levelIndex = currentLevelIndex % packageSize;
		if (pack == null || pack.getNumberOfLevelsPut() == packageSize) {
			pack = new DailyPackage(dailyPackageIdPrefix + "-" + packageIndex,
					packageSize, difficulties[packageIndex], collection);
			pack.setDisplayName(dayName + Integer.toString(packageIndex + 1));
			collection.setPackage(packageIndex, pack);
		}
		Difficulty packageDifficulty = pack.getDifficulty();
		random.setSeed(millis + currentLevelIndex);
		long seed = QualityPuzzleGenerator.generateSeed(random, random,
				packageDifficulty);

		DailyLevel level = new DailyLevel(seed, random, pack,
				Integer.toString(levelIndex + 1));
		pack.putLevel(level);
		currentLevelIndex++;
		progress = (float) currentLevelIndex
				/ (packageSize * collection.size());
		return progress == 1;
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public void finish() {
		while (!update()) {
			// continue until finished
		}
	}

	@Override
	public LevelPackageCollection get() {
		if (getProgress() != 1) {
			throw new IllegalStateException("Not finished generating");
		}
		return collection;
	}

	private static int getNumberOfPackages(GregorianCalendarInterface date,
			Random random) {
		int minPackages = LksBhmGame.getGame(Mona.class).getSettings().statics
				.getMinimumNumberOfDailyPackages();
		int maxPackages = LksBhmGame.getGame(Mona.class).getSettings().statics
				.getMaximumNumberOfDailyPackages();
		random.setSeed(date.getTimeInMillis());
		return random.nextInt(maxPackages - minPackages) + minPackages;
	}

	private static void fillWithDifficulty(Difficulty d,
			Difficulty[] difficulties) {
		for (int i = 0; i < difficulties.length; i++) {
			difficulties[i] = d;
		}
	}

	private static Difficulty[] getDifficulties(
			GregorianCalendarInterface date, Random random) {
		Difficulty[] difficulties = new Difficulty[getNumberOfPackages(date,
				random)];
		switch (date.getDayOfWeek()) {
		case GregorianCalendarInterface.MONDAY:
			fillWithDifficulty(Difficulty.VERY_EASY, difficulties);
			break;
		case GregorianCalendarInterface.TUESDAY:
			fillWithDifficulty(Difficulty.EASY, difficulties);
			break;
		case GregorianCalendarInterface.WEDNESDAY:
			fillWithDifficulty(Difficulty.MEDIUM, difficulties);
			break;
		case GregorianCalendarInterface.THURSDAY:
			fillWithDifficulty(Difficulty.HARD, difficulties);
			break;
		case GregorianCalendarInterface.FRIDAY:
			fillWithDifficulty(Difficulty.VERY_HARD, difficulties);
			break;
		default:
			random.setSeed(date.getTimeInMillis());
			for (int i = 0; i < difficulties.length; i++) {
				difficulties[i] = Difficulty.fromNumber(random.nextInt(5));
			}
			Arrays.sort(difficulties);
			break;
		}
		return difficulties;
	}

	private static String shortDayNameFromIndex(int weekday) {
		weekday = (weekday - 1) % 7 + 1;
		switch (weekday) {
		case GregorianCalendarInterface.MONDAY:
			return "MON";
		case GregorianCalendarInterface.TUESDAY:
			return "TUE";
		case GregorianCalendarInterface.WEDNESDAY:
			return "WED";
		case GregorianCalendarInterface.THURSDAY:
			return "THU";
		case GregorianCalendarInterface.FRIDAY:
			return "FRI";
		case GregorianCalendarInterface.SATURDAY:
			return "SAT";
		case GregorianCalendarInterface.SUNDAY:
			return "SUN";
		}
		throw new RuntimeException();
	}
}
