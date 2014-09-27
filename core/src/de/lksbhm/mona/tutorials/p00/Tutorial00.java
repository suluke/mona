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
import de.lksbhm.mona.tutorials.Tutorial;
import de.lksbhm.mona.ui.screens.AbstractLevelScreen;

public class Tutorial00 extends Tutorial {

	private Window overlay;
	private Part currentPart;
	private final Table content = new Table();
	private final ClickListener clickForNextListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			currentPart = currentPart.next();
			applyCurrentPart();
		};
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
		GREETING, INTRO_1;

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
		Skin skin = LksBhmGame.getGame().getDefaultSkin();

		if (overlay == null) {
			// Setup overlay
			overlay = new Window("", skin);
			overlay.setMovable(false);
			overlay.setResizable(false);
			overlay.setResizeBorder(0);
			// Catch all input
			overlay.setModal(true);
			overlay.addListener(clickForNextListener);
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
		default:
			break;
		}
	}

	private void greetingPart() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		// Setup overlay content
		Label welcome = new Label("Welcome to\nMona!", skin);
		welcome.setAlignment(Align.center);
		content.add(welcome).row();
		String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to proceed)"
				: "(click to proceed)";
		Label continueHint = new Label(continueHintString, skin);
		continueHint.setFontScale(.5f);
		content.add(continueHint);
	}

	private void intro1Part() {
		float width = getLevel().getView().getStage().getWidth();

		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		// Setup overlay content
		Label introText = new Label(
				"In the background you can see the playboard which consists of tiles.",
				skin);
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		content.add(introText).width(width * .9f).row();
		String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to proceed)"
				: "(click to proceed)";
		Label continueHint = new Label(continueHintString, skin);
		continueHint.setFontScale(.5f);
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