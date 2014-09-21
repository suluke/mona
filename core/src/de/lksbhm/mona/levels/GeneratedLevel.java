package de.lksbhm.mona.levels;

import java.util.Random;

public class GeneratedLevel extends Level {

	private final long seed;
	private final Random random;

	public GeneratedLevel(long seed, Random random, LevelPackage pack, String id) {
		super(pack, id);
		this.random = random;
		this.seed = seed;
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		random.setSeed(seed);
		return LevelPuzzleGenerator.generate(this, random, 1.f, 1.f);
	}

	protected long getSeed() {
		return seed;
	}

	protected Random getRandom() {
		return random;
	}
}
