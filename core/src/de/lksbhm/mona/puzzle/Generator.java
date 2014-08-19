package de.lksbhm.mona.puzzle;

import java.util.Random;

import com.badlogic.gdx.math.RandomXS128;

import de.lksbhm.mona.puzzle.representations.grouped.GroupedTile;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTileBoard;
import de.lksbhm.mona.puzzle.representations.grouped.TileGroupType;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoard;
import de.lksbhm.mona.puzzle.representations.linked.LinkedTileBoardGenerator;
import de.lksbhm.mona.puzzle.representations.undirected.UndirectedTileBoard;

public class Generator {
	private Generator() {

	}

	public static Puzzle generate(int width, int height, Random random,
			float straightStoneProbability, float edgeStoneProbability) {
		LinkedTileBoard path = LinkedTileBoardGenerator.generateBoard(width,
				height, random);
		UndirectedTileBoard solution = path.toUndirected();
		Puzzle result = new Puzzle(solution, width, height);
		placeStones(result, random, straightStoneProbability,
				edgeStoneProbability);
		path.dispose();
		return result;
	}

	private static void placeStones(Puzzle puzzle, Random random,
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

	public static void main(String[] args) {
		// good example seeds: (1,2)
		Puzzle p = generate(10, 10, new RandomXS128(1, 2), 1.0f, 1.0f);
		System.out.println(p);
		System.out.println("---------------------------");
		System.out.println(p.getSolution());
	}
}
