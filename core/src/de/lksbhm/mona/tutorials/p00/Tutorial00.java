package de.lksbhm.mona.tutorials.p00;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;

public class Tutorial00 extends
		AbstractTutorial<de.lksbhm.mona.tutorials.p00.Tutorial00.Part> {

	private final ClickListener clickForNextListener = new ClickForNextListener();
	private final ClickListener clickForRemoveOverlayListener = new ClickForRemoveOverlayListener();
	private final PuzzleChangedListener puzzleChangedListener = new ListenForPuzzleChangedListener();
	private Label continueHint;

	public static enum Part implements Iterator<Part> {
		GREETING, INTRO_1, INTRO_2, INTRO_3, INTRO_4, INTRO_5;

		@Override
		public boolean hasNext() {
			Part[] values = Part.values();
			int ordinal = this.ordinal();
			return ordinal + 1 < values.length;
		}

		@Override
		public Part next() {
			if (!hasNext()) {
				return null;
			}
			Part[] values = Part.values();
			int ordinal = this.ordinal();
			return values[ordinal + 1];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public Tutorial00(Level level) {
		super(level);
	}

	@Override
	protected Part getFirstPart() {
		return Part.GREETING;
	}

	@Override
	protected void setupContentForCurrentPart() {
		switch (getCurrentPart()) {
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
	}

	private void greetingPart() {
		getOverlay().addListener(clickForNextListener);
		// Setup overlay content
		Label welcome = new Label("Welcome to\nMona!", getSkin());
		welcome.setAlignment(Align.center);
		getContent().add(welcome).row();
		getContent().add(continueHint);
	}

	private void intro1Part() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"In the background you can see the tiles of the playboard.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro2Part() {
		getOverlay().addListener(clickForRemoveOverlayListener);
		getLevel().getPuzzle().addChangeListener(puzzleChangedListener);

		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"You can connect the tiles by dragging your finger from any tile to a neighboring one.\nTry it!",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro3Part() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Great!\nDid you see the blue perl in the lower left? The goal of the game is to draw a single closed path that includes all of the perls.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro4Part() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Keep the following in mind, though: The path must NOT bend inside a blue perl, but there has to be a turn in the path in EXACTLY ONE of the connected neighboring tiles.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro5Part() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label("Try to solve this level on your own now.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	@Override
	protected void start() {
		String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to proceed)"
				: "(click to proceed)";
		continueHint = new Label(continueHintString, getSkin());
		continueHint.setFontScale(.5f);

		super.start();
	}

	@Override
	protected void end() {
		getOverlay().removeListener(clickForNextListener);
		getOverlay().removeListener(clickForRemoveOverlayListener);
		getLevel().getPuzzle().removeChangeListener(puzzleChangedListener);
		super.end();
	}

	@Override
	public String getCanonicalClassName() {
		return "de.lksbhm.mona.tutorials.p00.Tutorial00";
	}
}