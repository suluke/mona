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
		clickListener.cancel();
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
			clickListener.cancel();
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
			if (currentPiece != null && currentPiece != startPiece
					&& startPiece.isNeighborOf(currentPiece)) {
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
					startPiece.disconnect(currentPiece);
				} else {
					// make connection
					Direction neighborDir = startPiece
							.getDirectionOfNeighbor(currentPiece);
					startPiece.pushInOutDirection(neighborDir);
					currentPiece.pushInOutDirection(neighborDir.getOpposite());
					preventDisconnect = true;
					lastConnected = startPiece;
				}
				startPiece = currentPiece;
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
		public ClickListener() {
			// setTapCountInterval(0.1f);
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (!dragListener.isDragging()) {
				switch (getTapCount()) {
				case 1: {
					Piece clicked = PuzzleActorCoordinateHelper
							.coordsToTileIncludingPadding(actor, x, y);
					if (clicked != null) {
						clicked.setInOutDirection(Direction.NONE,
								Direction.NONE);
					}
					break;
				}
				case 2: {
					actor.getPuzzle().clearInOuDirections();
					break;
				}
				}
			}
		}
	}
}