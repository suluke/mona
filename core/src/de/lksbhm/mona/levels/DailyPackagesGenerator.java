package de.lksbhm.mona.levels;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;

class DailyPackagesGenerator {
	private static final Random random = new RandomXS128();

	private static int getNumberOfPackages(Calendar date) {
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

	private static Difficulty[] getDifficulties(Calendar date) {
		Difficulty[] difficulties = new Difficulty[getNumberOfPackages(date)];
		switch (date.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			fillWithDifficulty(Difficulty.VERY_EASY, difficulties);
			break;
		case Calendar.TUESDAY:
			fillWithDifficulty(Difficulty.EASY, difficulties);
			break;
		case Calendar.WEDNESDAY:
			fillWithDifficulty(Difficulty.MEDIUM, difficulties);
			break;
		case Calendar.THURSDAY:
			fillWithDifficulty(Difficulty.HARD, difficulties);
			break;
		case Calendar.FRIDAY:
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

	public static LevelPackageCollection getDailyPackages(Calendar date) {
		Difficulty[] difficulties = getDifficulties(date);
		LevelPackageCollection collection = new LevelPackageCollection(
				difficulties.length);
		long seed;
		long millis = date.getTimeInMillis();
		Date d = new Date(millis);
		String dayName;
		LevelPackage newPack;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < difficulties.length; i++) {
			random.setSeed(millis + i);
			seed = random.nextLong();
			dayName = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
					Locale.getDefault());
			newPack = new DailyPackage(formatter.format(d) + "-" + i,
					difficulties[i], collection, seed, random);
			newPack.setDisplayName(dayName + Integer.toString(i + 1));
			collection.setPackage(i, newPack);
		}
		return collection;
	}
}
