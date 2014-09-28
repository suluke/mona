package de.lksbhm.mona.tutorials.p00;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.tutorials.Tutorial;
import de.lksbhm.mona.ui.screens.AbstractLevelScreen;

public class Tutorial00 extends Tutorial {

	private Window overlay;
	private Part currentPart;
	private final Table content = new Table();
	private Skin skin;
	private Label continueHint;
	private final ClickListener clickForNextListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			overlay.removeListener(this);
			currentPart = currentPart.next();
			applyCurrentPart();
		};
	};
	private final ClickListener clickForRemoveOverlayListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			overlay.removeListener(this);
			overlay.addAction(Actions.sequence(Actions.alpha(0, .6f),
					Actions.removeActor(), Actions.alpha(1)));
		};
	};
	private final PuzzleChangedListener puzzleChangedListener = new PuzzleChangedListener() {
		@Override
		public void onChange() {
			getLevel().getPuzzle().removeChangeListener(this);
			currentPart = currentPart.next();
			applyCurrentPartAction.restart();
			dispatchCurrentPart();
			overlay.addAction(Actions.sequence(Actions.alpha(0),
					Actions.alpha(1, .6f)));
		}
	};
	private final Action applyCurrentPartAction = new Action() {
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

	private enum Part {
		GREETING, INTRO_1, INTRO_2, INTRO_3, INTRO_4, INTRO_5;

		public boolean hasNext() {
			Part[] values = Part.values();
			int ordinal = this.ordinal();
			return ordinal + 1 < values.length;
		}

		public Part next() {
			if (!hasNext()) {
				return null;
			}
			Part[] values = Part.values();
			int ordinal = this.ordinal();
			return values[ordinal + 1];
		}
	}

	public Tutorial00(Level level) {
		super(level);
	}

	@Override
	protected void start() {
		skin = LksBhmGame.getGame().getDefaultSkin();

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
		if (continueHint == null) {
			String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to proceed)"
					: "(click to proceed)";
			continueHint = new Label(continueHintString, skin);
			continueHint.setFontScale(.5f);
		}
		currentPart = Part.GREETING;
		dispatchCurrentPart();
	}

	private void applyCurrentPart() {
		if (currentPart == null) {
			end();
			return;
		}
		applyCurrentPartAction.restart();
		content.addAction(Actions.sequence(Actions.alpha(0, .6f),
				applyCurrentPartAction, Actions.alpha(1, .6f)));
	}

	private void dispatchCurrentPart() {
		content.clearChildren();
		switch (currentPart) {
		case GREETING:
			greetingPart();
			break;
		case INTRO_1:
			intro1Part();
			break;
		case INTRO_2:
			intro2Part();
			break;
		case INTRO_3:
			intro3Part();
			break;
		case INTRO_4:
			intro4Part();
			break;
		case INTRO_5:
			intro5Part();
			break;
		default:
			break;
		}
		if (!overlay.hasParent()) {
			getLevel().getView().getStage().addActor(overlay);
		}
	}

	private void greetingPart() {
		overlay.addListener(clickForNextListener);
		// Setup overlay content
		Label welcome = new Label("Welcome to\nMona!", skin);
		welcome.setAlignment(Align.center);
		content.add(welcome).row();
		content.add(continueHint);
	}

	private void intro1Part() {
		overlay.addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"In the background you can see the playboard which consists of tiles.",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		content.add(continueHint);
	}

	private void intro2Part() {
		overlay.addListener(clickForRemoveOverlayListener);
		getLevel().getPuzzle().addChangeListener(puzzleChangedListener);

		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"You can connect the tiles by dragging your finger from any tile to a neighboring one.\nTry it!",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		content.add(continueHint);
	}

	private void intro3Part() {
		overlay.addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Great!\nDid you see the blue perl in the lower left? The goal of the game is to draw a single closed path that includes all of the perls.",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		content.add(continueHint);
	}

	private void intro4Part() {
		overlay.addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Keep the following in mind, though: The path must NOT bend inside a blue perl, but there has to be a turn in the path in EXACTLY ONE of the connected neighboring tiles.",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		content.add(continueHint);
	}

	private void intro5Part() {
		overlay.addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label("Try to solve this level on your own now.",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		content.add(continueHint);
	}

	@Override
	protected void end() {
		currentPart = null;
		if (overlay.hasParent()) {
			overlay.addAction(Actions.sequence(Actions.alpha(0, .6f),
					Actions.removeActor(), Actions.alpha(1)));
		}
	}
}