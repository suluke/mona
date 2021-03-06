package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LiterallyDefinedLevel extends Level {

	private final Piece.Type[][] tileTypes;
	private final DirectionalTileBoard solution;

	public LiterallyDefinedLevel(Piece.Type[][] tileTypes,
			DirectionalTileBoard solution, LevelPackage pack, String id) {
		super(pack, id);
		this.solution = solution;
		this.tileTypes = tileTypes;
	}

	@Override
	protected LevelPuzzle instantiatePuzzle() {
		// don't forget the copying, or our solution will be disposed on puzzle
		// disposal
		LevelPuzzle puzzle = new LevelPuzzle(this, solution.shallowCopy(),
				tileTypes.length, tileTypes[0].length);
		for (Piece piece : puzzle) {
			piece.setType(tileTypes[piece.getX()][piece.getY()]);
		}
		return puzzle;
	}

	@Override
	protected DirectionalTileBoard instantiateSolution() {
		return solution;
	}
}
