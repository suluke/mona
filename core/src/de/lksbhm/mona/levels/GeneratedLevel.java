package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.mona.puzzle.Piece.Type;

public class GeneratedLevel extends Level {

	private final long seed;
	private final Random random;
	private final int[][] invisibleTiles;
	private final Difficulty difficulty;

	public GeneratedLevel(long seed, Random random, LevelPackage pack, String id) {
		this(seed, random, pack, id, null, null);
	}

	public GeneratedLevel(long seed, Random random, LevelPackage pack,
			String id, Difficulty d, int[][] invisibleTiles) {
		super(pack, id);
		this.random = random;
		this.seed = seed;
		this.difficulty = d;
		this.invisibleTiles = invisibleTiles;
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		random.setSeed(seed);
		LevelPuzzle puzzle;
		if (difficulty == null) {
			puzzle = LevelPuzzleGenerator.generate(this, random, 1.f, 1.f);
		} else {
			puzzle = LevelPuzzleGenerator.generate(this, difficulty, random,
					1.f, 1.f);
		}
		if (invisibleTiles != null) {
			for (int[] coord : invisibleTiles) {
				puzzle.getTile(coord[0], coord[1]).setType(Type.INVISIBLE);
			}
		}
		return puzzle;
	}

	public Class<? extends Random> getRandomType() {
		return random.getClass();
	}

	protected Random getRandom() {
		return random;
	}

	@Override
	public String toString() {
		return seed + "@" + getRandomType().getSimpleName();
	}
}
