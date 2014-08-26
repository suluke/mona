package de.lksbhm.mona.ui.actors;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;

class PuzzleActorCoordinateHelper {
	private PuzzleActorCoordinateHelper() {
	}

	public static Piece coordsToTile(PuzzleActor actor, float x, float y) {

		float cellWidth = actor.getCellWidth();
		float cellHeight = actor.getCellHeight();
		Puzzle p = actor.getPuzzle();

		x -= actor.getMarginLeft();
		int tileX = (int) (x / (cellWidth + actor.getPaddingWidth()));
		x %= cellWidth + actor.getPaddingWidth();

		if (!actor.isInvertY()) {
			y = actor.getHeight() - y;
		}
		y -= actor.getMarginTop();
		int tileY = (int) (y / (cellHeight + actor.getPaddingHeight()));
		y %= cellHeight + actor.getPaddingHeight();
		if (tileX < 0 || tileX >= p.getWidth() || tileY < 0
				|| tileY >= p.getHeight()) {
			return null;
		}

		if (x > cellWidth || y > cellHeight) {
			return null;
		}
		return actor.getPuzzle().getTile(tileX, tileY);
	}

	public static float getTileOriginX(PuzzleActor actor, Piece tile) {
		return actor.getMarginLeft() + tile.getX()
				* (actor.getCellWidth() + actor.getPaddingWidth());
	}

	public static float getTileOriginY(PuzzleActor actor, Piece tile) {
		float result = actor.getMarginTop() + tile.getY()
				* (actor.getCellHeight() + actor.getPaddingHeight());
		if (actor.isInvertY()) {
			result = actor.getHeight() - result + actor.getCellHeight();
		}
		return result;
	}
}
