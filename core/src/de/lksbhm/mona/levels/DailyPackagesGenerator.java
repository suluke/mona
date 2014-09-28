package de.lksbhm.mona.levels;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.mona.Mona;

class DailyPackagesGenerator implements Loadable<LevelPackageCollection> {
	private final Random random = new RandomXS128();
	private final Difficulty[] difficulties;
	private final LevelPackageCollection collection;
	private float progress = 0;
	private final GeneratorRunnable generatorRunnable = new GeneratorRunnable();
	private final Thread generatorThread;
	private final GregorianCalendarInterface date;

	DailyPackagesGenerator(GregorianCalendarInterface date) {
		this.date = date;
		difficulties = getDifficulties(date, random);
		collection = new LevelPackageCollection(difficulties.length);
		generatorThread = new Thread(generatorRunnable);
		generatorThread.start();
	}

	@Override
	public boolean update() {
		progress = generatorRunnable.getProgress();
		return progress == 1;
	}

	@Override
	public float getProgress() {
		return progress;
	}

	@Override
	public void finish() {
		try {
			generatorThread.join();
			progress = generatorRunnable.getProgress();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
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

	private class GeneratorRunnable implements Runnable {

		private float progress = 0;

		public float getProgress() {
			return progress;
		}

		@Override
		public void run() {
			long seed;
			long millis = date.getTimeInMillis();
			String dayName;
			LevelPackage newPack;
			String dailyPackageIdPrefix = LksBhmGame.getGame(Mona.class)
					.getSettings().statics.getDailyPackageIdPrefix();
			progress = 0;
			final float progressPerPackage = 1 / difficulties.length;
			for (int i = 0; i < difficulties.length; i++) {
				random.setSeed(millis + i);
				seed = random.nextLong();
				dayName = shortDayNameFromIndex(date.getDayOfWeek());
				newPack = new DailyPackage(dailyPackageIdPrefix + "-" + i,
						difficulties[i], collection, seed, random);
				newPack.setDisplayName(dayName + Integer.toString(i + 1));
				collection.setPackage(i, newPack);
				progress += progressPerPackage;
			}
			progress = 1;
		}

	}
}
