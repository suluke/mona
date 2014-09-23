package de.lksbhm.mona.levels;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.mona.Mona;

class DailyPackagesGenerator {
	private static final Random random = new RandomXS128();

	private static int getNumberOfPackages(GregorianCalendarInterface date) {
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

	private static Difficulty[] getDifficulties(GregorianCalendarInterface date) {
		Difficulty[] difficulties = new Difficulty[getNumberOfPackages(date)];
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

	public static LevelPackageCollection getDailyPackages(
			GregorianCalendarInterface date) {
		Difficulty[] difficulties = getDifficulties(date);
		LevelPackageCollection collection = new LevelPackageCollection(
				difficulties.length);
		long seed;
		long millis = date.getTimeInMillis();
		String dayName;
		LevelPackage newPack;
		String dailyPackageIdPrefix = LksBhmGame.getGame(Mona.class)
				.getSettings().statics.getDailyPackageIdPrefix();
		for (int i = 0; i < difficulties.length; i++) {
			random.setSeed(millis + i);
			seed = random.nextLong();
			dayName = shortDayNameFromIndex(date.getDayOfWeek());
			newPack = new DailyPackage(dailyPackageIdPrefix + "-" + i,
					difficulties[i], collection, seed, random);
			newPack.setDisplayName(dayName + Integer.toString(i + 1));
			collection.setPackage(i, newPack);
		}
		return collection;
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
