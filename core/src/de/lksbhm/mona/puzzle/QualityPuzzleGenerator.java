package de.lksbhm.mona.puzzle;

import java.util.Random;

import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.levels.Difficulty;
import de.lksbhm.mona.puzzle.Piece.Type;

public class QualityPuzzleGenerator {
	private QualityPuzzleGenerator() {

	}

	public static Pair<Long, Puzzle> generateRandomSeedAndPuzzle(Random r) {
		long seed;
		Puzzle p;
		do {
			seed = Generator.random.nextLong();
			r.setSeed(seed);
			p = Generator.generate(r, 1.f, 1.f);
		} while (!isGoodQuality(p));
		return new Pair<Long, Puzzle>(seed, p);
	}

	private static boolean isGoodQuality(Puzzle p) {
		boolean result = true;
		int tiles = p.getWidth() * p.getHeight();
		int nonEmpty = getNonEmptyTilesCount(p);
		result &= nonEmpty != 0;
		result &= (nonEmpty / (float) tiles) > 0.125f;
		return result;
	}

	private static int getNonEmptyTilesCount(Puzzle p) {
		int result = 0;
		for (Piece tile : p) {
			if (tile.getType() != Type.EMPTY) {
				result++;
			}
		}
		return result;
	}

	public static Pair<Long, Puzzle> generateSeedAndPuzzle(
			Random seedGenerator, Random random, Difficulty difficulty) {
		long seed;
		Puzzle p;
		do {
			seed = seedGenerator.nextLong();
			random.setSeed(seed);
			p = Generator.generate(difficulty, random, 1.f, 1.f);
		} while (!isGoodQuality(p));
		return new Pair<Long, Puzzle>(seed, p);
	}

	public static long generateSeed(Random random) {
		Pair<Long, Puzzle> result = generateRandomSeedAndPuzzle(random);
		result.getSecond().dispose();
		return result.getFirst();
	}

	public static long generateSeed(Random seedGenerator, Random random,
			Difficulty difficulty) {
		Pair<Long, Puzzle> result = generateSeedAndPuzzle(seedGenerator,
				random, difficulty);
		result.getSecond().dispose();
		return result.getFirst();
	}
}
