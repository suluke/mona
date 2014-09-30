package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.QualityPuzzleGenerator;

public class DailyPackage extends LevelPackage {

	private final Random random;
	private final long seed;

	public DailyPackage(String id, Difficulty difficulty,
			LevelPackageCollection collection, long seed, Random random) {
		super(id, LksBhmGame.getGame(Mona.class).getSettings().statics
				.getLevelsPerPackage(), difficulty, collection);
		this.random = random;
		this.seed = seed;
	}

	@Override
	protected void loadLevels() {
		int size = getSize();
		random.setSeed(seed);
		Difficulty d = getDifficulty();
		for (int i = 0; i < size; i++) {
			long seed = QualityPuzzleGenerator.generateSeed(random, random, d);
			DailyLevel level = new DailyLevel(seed, random, this,
					Integer.toString(i + 1));
			putLevel(level);
		}
	}

}
