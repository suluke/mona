package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.mona.puzzle.Generator;
import de.lksbhm.mona.puzzle.Puzzle;

public class DailyLevel extends GeneratedLevel {

	public DailyLevel(long seed, Random random, LevelPackage pack, String id) {
		super(seed, random, pack, id);
	}

	@Override
	protected Puzzle instantiatePuzzle() {
		Random random = getRandom();
		random.setSeed(getSeed());
		return Generator.generate(getPackage().getDifficulty(), random, 1.f,
				1.f);
	}
}
