package de.lksbhm.mona.levels;

import java.util.Random;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.Settings;
import de.lksbhm.mona.puzzle.Generator;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoard;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoardGenerator;

public class LevelPuzzleGenerator {

	private static int generateWidth(Difficulty difficulty, Random random) {
		Settings settings = LksBhmGame.getGame(Mona.class).getSettings();
		int minWidth = settings.statics
				.getMinPuzzleWidthForDifficulty(difficulty);
		int maxWidth = settings.statics
				.getMaxPuzzleWidthForDifficulty(difficulty);
		int width = minWidth + random.nextInt(maxWidth - minWidth + 1);
		return width;
	}

	private static int generateHeight(Difficulty difficulty, Random random) {
		Settings settings = LksBhmGame.getGame(Mona.class).getSettings();
		int minHeight = settings.statics
				.getMinPuzzleHeightForDifficulty(difficulty);
		int maxHeight = settings.statics
				.getMaxPuzzleHeightForDifficulty(difficulty);
		int height = minHeight + random.nextInt(maxHeight - minHeight + 1);
		return height;
	}

	public static LevelPuzzle generate(Level level, Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		Difficulty d = Difficulty.fromNumber(random.nextInt(5));
		return generate(level, d, random, straightStoneProbability,
				edgeStoneProbability);
	}

	public static LevelPuzzle generate(Level level, Difficulty difficulty,
			Random random, float straightStoneProbability,
			float edgeStoneProbability) {
		int width = generateWidth(difficulty, random);
		int height = generateHeight(difficulty, random);
		return generate(level, width, height, random, straightStoneProbability,
				edgeStoneProbability);
	}

	public static LevelPuzzle generate(Level level, int width, int height,
			Random random, float straightStoneProbability,
			float edgeStoneProbability) {
		DirectionalTileBoard solution = generateSolution(width, height, random);
		LevelPuzzle result = new LevelPuzzle(level, solution, width, height);
		Generator.placeStones(result, random, straightStoneProbability,
				edgeStoneProbability);
		return result;
	}

	public static DirectionalTileBoard generateSolution(Random random) {
		Difficulty d = Difficulty.fromNumber(random.nextInt(5));
		return generateSolution(d, random);
	}

	public static DirectionalTileBoard generateSolution(Difficulty difficulty,
			Random random) {
		int width = generateWidth(difficulty, random);
		int height = generateHeight(difficulty, random);
		return generateSolution(width, height, random);
	}

	public static DirectionalTileBoard generateSolution(int width, int height,
			Random random) {
		LinkedTileBoard path = LinkedTileBoardGenerator.generateBoard(width,
				height, random);
		DirectionalTileBoard solution = path.toUndirected();
		path.dispose();
		return solution;
	}
}
