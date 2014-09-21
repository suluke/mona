package de.lksbhm.mona.levels;

import java.util.Random;

public class DailyLevel extends GeneratedLevel {

	public DailyLevel(long seed, Random random, LevelPackage pack, String id) {
		super(seed, random, pack, id);
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		Random random = getRandom();
		random.setSeed(getSeed());
		return LevelPuzzleGenerator.generate(this,
				getPackage().getDifficulty(), random, 1.f, 1.f);
	}
}
