package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import de.lksbhm.mona.puzzle.Piece;
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
						startPiece.disconnectNeighbor(currentPiece, true);
					} else {
						// make connection
						Direction neighborDir = startPiece
								.getDirectionOfNeighbor(currentPiece);
						startPiece.pushInOutDirection(neighborDir, false);
						currentPiece.pushInOutDirection(
								neighborDir.getOpposite(), false);
						actor.getPuzzle().notifyOnChange();
						actor.onAddConnection();
						preventDisconnect = true;
						lastConnected = startPiece;
					}
					startPiece = currentPiece;
				} else if (currentPiece.getX() == startPiece.getX()
						|| currentPiece.getY() == startPiece.getY()) {
					if (actor.getPuzzle().tryConnectPiecesStraight(startPiece,
							currentPiece)) {
						actor.getPuzzle().notifyOnChange();
						startPiece = currentPiece;
					}
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
							actor.onClearAllTileConnections();
							break;
						}
						// continue with 1
					}
					case 1: {
						clicked.setInOutDirection(Direction.NONE,
								Direction.NONE, true);
						actor.onClearTileConnections();
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