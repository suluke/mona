package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.Puzzle;

class PuzzleActorCoordinateHelper {
	private PuzzleActorCoordinateHelper() {
	}

	private static final Vector2 tmp = new Vector2(0, 0);

	/**
	 * 
	 * @param actor
	 * @param x
	 *            in local coordinates
	 * @param y
	 *            in local coordinates
	 * @return
	 */
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
		Piece tile = actor.getPuzzle().getTile(tileX, tileY);
		if (tile != null && tile.getType() == Type.INVISIBLE) {
			return null;
		}
		return tile;
	}

	/**
	 * 
	 * @param actor
	 * @param x
	 *            in local coordinates
	 * @param y
	 *            in local coordinates
	 * @return
	 */
	public static Piece coordsToTileIncludingPadding(PuzzleActor actor,
			float x, float y) {
		float cellWidth = actor.getCellWidth();
		float cellHeight = actor.getCellHeight();
		float paddingWidth = actor.getPaddingWidth();
		float paddingHeight = actor.getPaddingHeight();
		Puzzle p = actor.getPuzzle();

		x -= actor.getMarginLeft();
		x += paddingWidth / 2;
		int tileX = (int) (x / (cellWidth + paddingWidth));
		x %= cellWidth + paddingWidth;

		if (!actor.isInvertY()) {
			y = actor.getHeight() - y;
		}
		y -= actor.getMarginTop();
		y += paddingHeight / 2;
		int tileY = (int) (y / (cellHeight + paddingHeight));
		y %= cellHeight + paddingHeight;
		if (tileX < 0 || tileX >= p.getWidth() || tileY < 0
				|| tileY >= p.getHeight()) {
			return null;
		}

		if (x > cellWidth + paddingWidth || y > cellHeight + paddingHeight) {
			return null;
		}
		Piece tile = actor.getPuzzle().getTile(tileX, tileY);
		if (tile != null && tile.getType() == Type.INVISIBLE) {
			return null;
		}
		return tile;
	}

	/**
	 * 
	 * @param actor
	 * @param tile
	 * @param drawing
	 *            whether or not the {@link PuzzleActor} is currently being
	 *            drawn. Necessary because {@link Actor#getX() x} may be
	 *            temporarily altered during drawing.
	 * @return x in stage coordinates
	 */
	public static float getTileOriginX(PuzzleActor actor, Piece tile,
			boolean drawing) {
		float x;
		if (drawing) {
			x = actor.getX();
		} else {
			tmp.set(0, 0);
			x = actor.localToStageCoordinates(tmp).x;
		}
		return x + actor.getMarginLeft() + tile.getX()
				* (actor.getCellWidth() + actor.getPaddingWidth());
	}

	/**
	 * 
	 * @param actor
	 * @param tile
	 * @param drawing
	 *            whether or not the {@link PuzzleActor} is currently being
	 *            drawn. Necessary because {@link Actor#getY() y} may be
	 *            temporarily altered during drawing.
	 * @return y in stage coordinates
	 */
	public static float getTileOriginY(PuzzleActor actor, Piece tile,
			boolean drawing) {
		float y;
		if (drawing) {
			y = actor.getY();
		} else {
			tmp.set(0, 0);
			y = actor.localToStageCoordinates(tmp).y;
		}

		float result = actor.getMarginTop() + tile.getY()
				* (actor.getCellHeight() + actor.getPaddingHeight());
		if (!actor.isInvertY()) {
			result = actor.getHeight() - result - actor.getCellHeight();
		}
		result += y;
		return result;
	}
}
