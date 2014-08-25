package de.lksbhm.mona.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

class PuzzleActorInput implements EventListener {
	private final PuzzleActor actor;
	private final DragListener dragListener = new DragListener();

	public PuzzleActorInput(PuzzleActor actor) {
		this.actor = actor;
	}

	@Override
	public boolean handle(Event event) {
		boolean handled = false;
		handled |= dragListener.handle(event);
		return handled;
	}

	private class DragListener extends
			com.badlogic.gdx.scenes.scene2d.utils.DragListener {
		@Override
		public void dragStart(InputEvent event, float x, float y, int pointer) {
			System.out.println("Start");
		}

		@Override
		public void drag(InputEvent event, float x, float y, int pointer) {
			System.out.println("drag");
		}

		@Override
		public void dragStop(InputEvent event, float x, float y, int pointer) {
			System.out.println("Stop");
		}
	}
}