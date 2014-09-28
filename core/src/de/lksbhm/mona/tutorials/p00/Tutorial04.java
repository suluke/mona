package de.lksbhm.mona.tutorials.p00;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.mona.levels.Level;

public class Tutorial04 extends
		AbstractTutorial<de.lksbhm.mona.tutorials.p00.Tutorial04.Part> {

	private final ClickListener clickForNextListener = new ClickForNextListener();
	private Label continueHint;

	public static enum Part implements Iterator<Part> {
		PART_1;

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

	public Tutorial04(Level level) {
		super(level);
	}

	@Override
	protected void start() {
		String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to play)"
				: "(click to play)";
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
		default:
			break;

		}
	}

	private void part1() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Good.\nThis is all you need to know. Continue playing, enjoy, and become a MONA master!",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}
}
