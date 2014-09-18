package de.lksbhm.mona.levels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;

class DailyPackagesGenerator {
	private static final Random random = new RandomXS128();

	public static int getNumberOfPackages(Calendar date) {
		int minPackages = LksBhmGame.getGame(Mona.class).getSettings().statics
				.getMinimumNumberOfDailyPackages();
		int maxPackages = LksBhmGame.getGame(Mona.class).getSettings().statics
				.getMaximumNumberOfDailyPackages();
		random.setSeed(date.getTimeInMillis());
		return random.nextInt(maxPackages - minPackages) + minPackages;
	}

	public static Difficulty getDifficultyForPackage(int index, Calendar date) {
		random.setSeed(date.getTimeInMillis() + index);
		return Difficulty.fromNumber(random.nextInt(5));
	}

	public static LevelPackageCollection getDailyPackages(Calendar date) {
		int numberOfPacks = getNumberOfPackages(date);
		LevelPackageCollection collection = new LevelPackageCollection(
				numberOfPacks);
		long seed;
		long millis = date.getTimeInMillis();
		Date d = new Date(millis);
		String dayName;
		LevelPackage newPack;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < numberOfPacks; i++) {
			random.setSeed(millis + i);
			seed = random.nextLong();
			dayName = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT,
					Locale.getDefault());
			newPack = new DailyPackage(formatter.format(d) + "-" + i,
					getDifficultyForPackage(i, date), collection, seed, random);
			newPack.setDisplayName(dayName + Integer.toString(i + 1));
			collection.setPackage(i, newPack);
		}
		return collection;
	}
}
