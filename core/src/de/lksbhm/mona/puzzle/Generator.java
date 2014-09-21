package de.lksbhm.mona.puzzle;

import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.Settings;
import de.lksbhm.mona.levels.Difficulty;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTile;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTileBoard;
import de.lksbhm.mona.puzzle.representations.grouped.TileGroupType;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoard;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoardGenerator;

public class Generator {
	private Generator() {

	}

	/**
	 * WARNING: java.util.Random's setSeed() will not guarantee same random
	 * values across platforms for same seed. Only use this Random instance if
	 * you really don't care about it's output.
	 */
	public static final Random random = new Random();

	public static Puzzle generate(float straightStoneProbability,
			float edgeStoneProbability) {
		long seed = random.nextLong();
		return generate(seed, straightStoneProbability, edgeStoneProbability);
	}

	public static Puzzle generate(long seed, float straightStoneProbability,
			float edgeStoneProbability) {
		// Don't use java.util.Random here, because behavior is
		// platform-dependent
		Random r = new RandomXS128(seed);
		return generate(r, straightStoneProbability, edgeStoneProbability);
	}

	public static Puzzle generate(Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		Difficulty d = Difficulty.fromNumber(random.nextInt(5));
		return generate(d, random, straightStoneProbability,
				edgeStoneProbability);
	}

	public static Puzzle generate(Difficulty d, Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		Settings settings = LksBhmGame.getGame(Mona.class).getSettings();
		int minWidth = settings.statics.getMinPuzzleWidthForDifficulty(d);
		int maxWidth = settings.statics.getMaxPuzzleWidthForDifficulty(d);
		int width = minWidth + random.nextInt(maxWidth - minWidth + 1);
		int minHeight = settings.statics.getMinPuzzleHeightForDifficulty(d);
		int maxHeight = settings.statics.getMaxPuzzleHeightForDifficulty(d);
		int height = minHeight + random.nextInt(maxHeight - minHeight + 1);
		return generate(width, height, random, straightStoneProbability,
				edgeStoneProbability);
	}

	public static Puzzle generate(int width, int height, Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		LinkedTileBoard path = LinkedTileBoardGenerator.generateBoard(width,
				height, random);
		DirectionalTileBoard solution = path.toUndirected();
		Puzzle result = new Puzzle(solution, width, height);
		placeStones(result, random, straightStoneProbability,
				edgeStoneProbability);
		path.dispose();
		return result;
	}

	public static void placeStones(Puzzle puzzle, Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		GroupedTileBoard groupedBoard = puzzle.getSolution()
				.toGroupedTileBoard();
		GroupedTile[][] groupedTiles = groupedBoard.getTiles();
		Piece[][] pieces = puzzle.getTiles();
		int width = puzzle.getWidth();
		int height = puzzle.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (groupedTiles[x][y].getType() == TileGroupType.STRAIGHT_STRAIGHT_EDGE) {
					if (random.nextFloat() >= 1 - straightStoneProbability) {
						pieces[x][y].setType(Piece.Type.STRAIGHT);
					}
				} else if (groupedTiles[x][y].getType() == TileGroupType.STRAIGHT_EDGE_STRAIGHT) {
					if (random.nextFloat() >= 1 - edgeStoneProbability) {
						pieces[x][y].setType(Piece.Type.EDGE);
					}
				}
			}
		}
		groupedBoard.dispose();
	}
}
