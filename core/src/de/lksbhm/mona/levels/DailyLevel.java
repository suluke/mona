package de.lksbhm.mona.levels;

import java.util.Random;

public class DailyLevel extends Level {

	private final long seed;
	private final Random random;

	public DailyLevel(long seed, Random random, LevelPackage pack, String id) {
		super(pack, id);
		this.seed = seed;
		this.random = random;
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		random.setSeed(seed);
		return LevelPuzzleGenerator.generate(this,
				getPackage().getDifficulty(), random, 1.f, 1.f);
	}
}
