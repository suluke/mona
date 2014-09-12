package de.lksbhm.mona.puzzle;

import java.util.Random;

import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.puzzle.Piece.Type;

public class QualityPuzzleGenerator {
	private QualityPuzzleGenerator() {

	}

	public static Pair<Long, Puzzle> generateSeedAndPuzzle(Random r) {
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
}
