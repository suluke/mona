package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import de.lksbhm.mona.puzzle.Piece;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.Direction;

class PuzzleActorInput implements EventListener {
	private final PuzzleActor actor;
	private final DragListener dragListener = new DragListener();
	private final ClickListener clickListener = new ClickListener();

	public PuzzleActorInput(PuzzleActor actor) {
		this.actor = actor;
	}

	@Override
	public boolean handle(Event event) {
		boolean handled = false;
		handled |= clickListener.handle(event);
		/*
		 * draglistener must come after clicklistener, otherwise clicklistener
		 * will not see if dragging took place
		 */
		handled |= dragListener.handle(event);
		return handled;
	}

	public void cancel() {
		if (dragListener.isDragging()) {
			dragListener.dragStop(null, 0, 0, 0);
			dragListener.cancel();
		}
	}

	private class DragListener extends
			com.badlogic.gdx.scenes.scene2d.utils.DragListener {

		private Piece startPiece;
		private boolean preventDisconnect = false;
		private Piece lastConnected;

		public DragListener() {
			this.setTapSquareSize(2);
		}

		@Override
		public void dragStart(InputEvent event, float x, float y, int pointer) {
			startPiece = PuzzleActorCoordinateHelper
					.coordsToTileIncludingPadding(actor, x, y);
		}

		@Override
		public void drag(InputEvent event, float x, float y, int pointer) {
			if (startPiece == null) {
				return;
			}
			Piece currentPiece = PuzzleActorCoordinateHelper
					.coordsToTileIncludingPadding(actor, x, y);
			if (currentPiece != null && currentPiece != startPiece) {
				if (startPiece.isNeighborOf(currentPiece)) {
					// allow disconnect if going backwards
					if (currentPiece == lastConnected) {
						preventDisconnect = false;
					}
					/*
					 * unset connection if going over it, but only if we did not
					 * create a connection yet
					 */
					if (!preventDisconnect
							&& startPiece.isConnectedWith(currentPiece)) {
						startPiece.disconnect(currentPiece, true);
					} else {
						// make connection
						Direction neighborDir = startPiece
								.getDirectionOfNeighbor(currentPiece);
						startPiece.pushInOutDirection(neighborDir, false);
						currentPiece.pushInOutDirection(
								neighborDir.getOpposite(), false);
						actor.getPuzzle().notifyOnChange();
						preventDisconnect = true;
						lastConnected = startPiece;
					}
					startPiece = currentPiece;
				} else if (currentPiece.getX() == startPiece.getX()) {
					int tileX = currentPiece.getX();
					int startY = startPiece.getY();
					int endY = currentPiece.getY();
					if (endY < startY) {
						int swap = endY;
						endY = startY;
						startY = swap;
					}
					Puzzle p = actor.getPuzzle();
					Piece tile1;
					Piece tile2;
					for (int tileY = startY; tileY < endY; tileY++) {
						tile1 = p.getTile(tileX, tileY);
						tile2 = p.getTile(tileX, tileY + 1);
						tile1.pushInOutDirection(Direction.DOWN, false);
						tile2.pushInOutDirection(Direction.UP, false);
					}
					actor.getPuzzle().notifyOnChange();
					startPiece = currentPiece;
				} else if (currentPiece.getY() == startPiece.getY()) {
					int tileY = currentPiece.getY();
					int startX = startPiece.getX();
					int endX = currentPiece.getX();
					if (endX < startX) {
						int swap = endX;
						endX = startX;
						startX = swap;
					}
					Puzzle p = actor.getPuzzle();
					Piece tile1;
					Piece tile2;
					for (int tileX = startX; tileX < endX; tileX++) {
						tile1 = p.getTile(tileX, tileY);
						tile2 = p.getTile(tileX + 1, tileY);
						tile1.pushInOutDirection(Direction.RIGHT, false);
						tile2.pushInOutDirection(Direction.LEFT, false);
					}
					actor.getPuzzle().notifyOnChange();
					startPiece = currentPiece;
				}
			}
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer) {
			// Reset everything here as drag may be called before dragStart (?)
			startPiece = null;
			preventDisconnect = false;
			lastConnected = null;
		}
	}

	private class ClickListener extends
			com.badlogic.gdx.scenes.scene2d.utils.ClickListener {
		private int lastClickedTileX = -1;
		private int lastClickedTileY = -1;

		public ClickListener() {
			// setTapCountInterval(0.1f);
		}

		@Override
		public void cancel() {
			super.cancel();
			lastClickedTileX = -1;
			lastClickedTileY = -1;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (!dragListener.isDragging()) {
				Piece clicked = PuzzleActorCoordinateHelper
						.coordsToTileIncludingPadding(actor, x, y);
				if (clicked != null) {
					int clickedX = clicked.getX();
					int clickedY = clicked.getY();
					switch (getTapCount()) {
					case 2: {
						if (lastClickedTileX == clickedX
								&& lastClickedTileY == clickedY) {
							actor.getPuzzle().clearInOutDirections(true);
							break;
						}
						// continue with 1
					}
					case 1: {
						clicked.setInOutDirection(Direction.NONE,
								Direction.NONE, true);
						break;
					}
					}
					lastClickedTileX = clicked.getX();
					lastClickedTileY = clicked.getY();
				}
			} else {
				cancel();
			}
		}
	}
}