package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.mona.puzzle.Piece.Type;

public class GeneratedLevel extends Level {

	private final long seed;
	private final Random random;
	private final int[][] invisibleTiles;

	public GeneratedLevel(long seed, Random random, LevelPackage pack, String id) {
		super(pack, id);
		this.random = random;
		this.seed = seed;
		invisibleTiles = null;
	}

	public GeneratedLevel(long seed, Random random, LevelPackage pack,
			String id, int[][] invisibleTiles) {
		super(pack, id);
		this.random = random;
		this.seed = seed;
		this.invisibleTiles = invisibleTiles;
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		random.setSeed(seed);
		LevelPuzzle puzzle = LevelPuzzleGenerator.generate(this, random, 1.f,
				1.f);
		if (invisibleTiles != null) {
			for (int[] coord : invisibleTiles) {
				puzzle.getTile(coord[0], coord[1]).setType(Type.INVISIBLE);
			}
		}
		return puzzle;
	}

	protected long getSeed() {
		return seed;
	}

	protected Random getRandom() {
		return random;
	}
}
