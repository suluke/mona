package de.lksbhm.mona.levels;

public class DailyPackage extends LevelPackage {
	private int levelsPut = 0;

	public DailyPackage(String id, int size, Difficulty difficulty,
			LevelPackageCollection collection) {
		super(id, size, difficulty, collection);
	}

	@Override
	public void putLevel(Level l) {
		super.putLevel(l);
		levelsPut++;
	}

	public int getNumberOfLevelsPut() {
		return levelsPut;
	}

	@Override
	protected void loadLevels() {
		// int size = getSize();
		// random.setSeed(seed);
		// Difficulty d = getDifficulty();
		// for (int i = 0; i < size; i++) {
		// long seed = QualityPuzzleGenerator.generateSeed(random, random, d);
		// DailyLevel level = new DailyLevel(seed, random, this,
		// Integer.toString(i + 1));
		// putLevel(level);
		// }
	}

}
