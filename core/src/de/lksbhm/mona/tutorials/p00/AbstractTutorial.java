package de.lksbhm.mona.tutorials.p00;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.tutorials.Tutorial;
import de.lksbhm.mona.ui.screens.AbstractLevelScreen;

abstract class AbstractTutorial<Part extends Iterator<Part>> extends Tutorial {
	private Window overlay;
	private final Table content = new Table();
	private final Skin skin = LksBhmGame.getGame().getDefaultSkin();;
	private Part currentPart;

	private final Action applyCurrentPartAction = new ApplyCurrentPartAction();

	public AbstractTutorial(Level level) {
		super(level);
	}

	protected class ClickForNextListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			overlay.removeListener(this);
			moveToNextPart();
		};
	}

	protected class ClickForRemoveOverlayListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			overlay.removeListener(this);
			overlay.addAction(Actions.sequence(Actions.alpha(0, .6f),
					Actions.removeActor(), Actions.alpha(1)));
		};
	}

	protected class ApplyCurrentPartAction extends Action {
		boolean finished = false;

		@Override
		public boolean act(float delta) {
			if (!finished) {
				finished = true;
				dispatchCurrentPart();
			}
			return true;
		}

		@Override
		public void restart() {
			finished = false;
		};
	};

	protected class ListenForPuzzleChangedListener implements
			PuzzleChangedListener {
		@Override
		public void onChange() {
			getLevel().getPuzzle().removeChangeListener(this);
			moveToNextPart();
		}
	};

	/**
	 * @return the content
	 */
	Table getContent() {
		return content;
	}

	/**
	 * @return the skin
	 */
	Skin getSkin() {
		return skin;
	}

	/**
	 * @return the currentPart
	 */
	Part getCurrentPart() {
		return currentPart;
	}

	/**
	 * @return the overlay
	 */
	Window getOverlay() {
		return overlay;
	}

	@Override
	protected void start() {
		if (overlay == null) {
			// Setup overlay
			overlay = new Window("", skin);
			overlay.setMovable(false);
			overlay.setResizable(false);
			overlay.setResizeBorder(0);
			// Catch all input
			overlay.setModal(true);
			overlay.setFillParent(true);

			// Add the overlay to the view screen
			AbstractLevelScreen<?> view = getLevel().getView();
			view.getStage().addActor(overlay);
			overlay.pack();

			Table buttonTable = overlay.getButtonTable();
			buttonTable.setFillParent(true);
			buttonTable.pack();

			// content.setFillParent(true);
			buttonTable.add(content);
			content.pack();
		}
		currentPart = getFirstPart();
		dispatchCurrentPart();
	}

	protected abstract Part getFirstPart();

	@Override
	protected void end() {
		currentPart = null;
		if (overlay.hasParent()) {
			overlay.addAction(Actions.sequence(Actions.alpha(0, .6f),
					Actions.removeActor(), Actions.alpha(1)));
		}
	}

	protected final void dispatchCurrentPart() {
		if (currentPart == null) {
			end();
			return;
		}
		getContent().clearChildren();
		setupContentForCurrentPart();
		if (!overlay.hasParent()) {
			getLevel().getView().getStage().addActor(overlay);
		}
	}

	/**
	 * 
	 * 
	 * Works around issues with concurrent modification exceptions, e.g. when in
	 * a listener call the next part is to be made visible and making it visible
	 * registers a new listener
	 */
	public void moveToNextPart() {
		currentPart = currentPart.next();
		applyCurrentPartAction.restart();
		if (overlay.hasParent()) {
			content.addAction(Actions.sequence(Actions.alpha(0, .6f),
					applyCurrentPartAction, Actions.alpha(1, .6f)));
		} else {
			overlay.addAction(Actions.sequence(Actions.alpha(0),
					applyCurrentPartAction, Actions.alpha(1, .6f)));
			AbstractLevelScreen<?> view = getLevel().getView();
			view.getStage().addActor(overlay);
		}
	}

	protected abstract void setupContentForCurrentPart();
}
