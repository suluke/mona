package de.lksbhm.mona.tutorials.p00;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.mona.levels.Level;

public class Tutorial03 extends
		AbstractTutorial<de.lksbhm.mona.tutorials.p00.Tutorial03.Part> {

	private final ClickListener clickForNextListener = new ClickForNextListener();
	private Label continueHint;

	public static enum Part implements Iterator<Part> {
		PART_1, PART_2, PART_3;

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

	public Tutorial03(Level level) {
		super(level);
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
		super.end();
	}

	@Override
	protected Part getFirstPart() {
		return Part.PART_1;
	}

	@Override
	protected void setupContentForCurrentPart() {
		switch (getCurrentPart()) {
		case PART_1:
			part1();
			break;
		case PART_2:
			part2();
			break;
		case PART_3:
			part3();
			break;
		default:
			break;

		}
	}

	private void part1() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Did you notice the red perl?\nIf you want to solve this level you must know that there are different rules applying on red perls than on blue ones.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
		getOverlay().addAction(
				Actions.sequence(Actions.alpha(0), Actions.delay(1.f),
						Actions.alpha(1, .6f)));
	}

	private void part2() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"While going through a red perl, the path has to bend. It must not bend in the neighboring tiles on either side of the path, though.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void part3() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Now show what you have learned and solve this level.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	@Override
	public String getCanonicalClassName() {
		return "de.lksbhm.mona.tutorials.p00.Tutorial03";
	}
}
