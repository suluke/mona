package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

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

	@Override
	protected DirectionalTileBoard instantiateSolution() {
		random.setSeed(seed);
		return LevelPuzzleGenerator.generateSolution(getPackage()
				.getDifficulty(), random);
	}
}
