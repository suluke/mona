package de.lksbhm.mona.tools.levels.conversion;

import de.lksbhm.mona.levels.GeneratedLevel;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

class GeneratedToLiteral {

	private final GeneratedLevel original;
	private final LiterallyDefinedLevel converted;
	private DirectionalTileBoard solution;
	private Type[][] tileTypes;

	public GeneratedToLiteral(GeneratedLevel level) {
		original = level;
		makeTileTypes();
		makeSolution();
		converted = new LiterallyDefinedLevel(tileTypes, solution,
				original.getPackage(), original.getLevelId());
	}

	private void makeSolution() {
		// TODO Auto-generated method stub

	}

	private void makeTileTypes() {
		Puzzle puzzle = original.getPuzzle();
		tileTypes = new Type[puzzle.getWidth()][puzzle.getHeight()];
		for (int x = 0; x < puzzle.getWidth(); x++) {
			for (int y = 0; y < puzzle.getHeight(); y++) {
				tileTypes[x][y] = puzzle.getTile(x, y).getType();
			}
		}
	}

	public LiterallyDefinedLevel getConverted() {
		return converted;
	}
}
