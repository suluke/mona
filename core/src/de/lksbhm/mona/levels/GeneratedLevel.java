package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.mona.puzzle.Generator;
import de.lksbhm.mona.puzzle.Puzzle;

public class GeneratedLevel extends Level {

	private final long seed;
	private final Random random;

	public GeneratedLevel(long seed, Random random, LevelPackage pack, String id) {
		super(pack, id);
		this.random = random;
		this.seed = seed;
	}

	@Override
	protected Puzzle instantiatePuzzle() {
		random.setSeed(seed);
		return Generator.generate(getPackage().getDifficulty(), random, 1.f,
				1.f);
	}
}
