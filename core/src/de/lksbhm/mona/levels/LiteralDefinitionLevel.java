package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LiteralDefinitionLevel extends Level {

	private final Piece.Type[][] tileTypes;
	private final DirectionalTileBoard solution;

	public LiteralDefinitionLevel(Piece.Type[][] tileTypes,
			DirectionalTileBoard solution, LevelPackage pack, String id) {
		super(pack, id);
		this.tileTypes = tileTypes;
		this.solution = solution;
	}

	@Override
	protected Puzzle instantiatePuzzle() {
		// don't forget the copying, or our solution will be disposed on puzzle
		// disposal
		Puzzle puzzle = new Puzzle(solution.shallowCopy(), tileTypes.length,
				tileTypes[0].length);
		for (Piece piece : puzzle) {
			piece.setType(tileTypes[piece.getX()][piece.getY()]);
		}
		return puzzle;
	}
}
