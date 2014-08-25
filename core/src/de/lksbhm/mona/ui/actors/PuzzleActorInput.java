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

	private class DragListener extends
			com.badlogic.gdx.scenes.scene2d.utils.DragListener {
		private Piece startPiece;

		@Override
		public void dragStart(InputEvent event, float x, float y, int pointer) {
			startPiece = PuzzleActorCoordinateHelper.coordsToTile(actor, x, y);
		}

		@Override
		public void drag(InputEvent event, float x, float y, int pointer) {
			Piece currentPiece = PuzzleActorCoordinateHelper.coordsToTile(
					actor, x, y);
			if (currentPiece != startPiece && currentPiece != null
					&& currentPiece.isNeighborOf(startPiece)) {
				Direction neighborDir = startPiece
						.getDirectionOfNeighbor(currentPiece);
				startPiece.pushInOutDirection(neighborDir);
				currentPiece.pushInOutDirection(neighborDir.getOpposite());
				startPiece = currentPiece;
			}
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer) {
			startPiece = null;
		}
	}

	private class ClickListener extends
			com.badlogic.gdx.scenes.scene2d.utils.ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (!dragListener.isDragging()) {
				Piece clicked = PuzzleActorCoordinateHelper.coordsToTile(actor,
						x, y);
				if (clicked != null) {
					clicked.setInOutDirection(Direction.NONE, Direction.NONE);
				}
			}
		}
	}
}