package de.lksbhm.mona.tutorials.p00;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.ui.actors.PuzzleModificationListener;

public class Tutorial01 extends
		AbstractTutorial<de.lksbhm.mona.tutorials.p00.Tutorial01.Part> {

	private final ClickListener clickForNextListener = new ClickForNextListener();
	private final ClickListener clickForRemoveOverlayListener = new ClickForRemoveOverlayListener();
	private final PuzzleChangedListener puzzleChangedListener = new ListenForPuzzleChangedListener();
	private Label continueHint;

	public static enum Part implements Iterator<Part> {
		CONGRATS, INTRO_1, INTRO_2, INTRO_3, INTRO_4;

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

	private final PuzzleModificationListener clearSingleTileListener = new PuzzleModificationListener() {
		@Override
		public void onClearTileConnections() {
			// TODO find a better way to circumvent concurrent modification
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					getLevel()
							.getView()
							.getPuzzleActor()
							.removeModificationListener(clearSingleTileListener);
				}
			});
			moveToNextPart();
		}

		@Override
		public void onClearAllTileConnections() {
		}

		@Override
		public void onAddConnection() {
		}
	};

	private final PuzzleModificationListener clearAllTilesListener = new PuzzleModificationListener() {
		@Override
		public void onClearTileConnections() {
		}

		@Override
		public void onClearAllTileConnections() {
			// TODO find a better way to circumvent concurrent modification
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					getLevel().getView().getPuzzleActor()
							.removeModificationListener(clearAllTilesListener);
				}
			});
			moveToNextPart();
		}

		@Override
		public void onAddConnection() {
		}
	};

	public Tutorial01(Level level) {
		super(level);
	}

	@Override
	protected Part getFirstPart() {
		return Part.CONGRATS;
	}

	@Override
	protected void setupContentForCurrentPart() {
		switch (getCurrentPart()) {
		case CONGRATS:
			congratsPart();
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
		default:
			break;
		}
	}

	private void congratsPart() {
		getOverlay().addListener(clickForNextListener);
		// Setup overlay content
		Label welcome = new Label("Well done.", getSkin());
		welcome.setAlignment(Align.center);
		getContent().add(welcome).row();
		getContent().add(continueHint);
	}

	private void intro1Part() {
		getOverlay().addListener(clickForNextListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"In this level, someone already added connections. Unfortunately, they are awefully wrong.",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro2Part() {
		getOverlay().addListener(clickForRemoveOverlayListener);
		getLevel().getView().getPuzzleActor()
				.addModificationListener(clearSingleTileListener);

		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"To clear the connections of a tile, click on the tile once.\nGo try it out!",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro3Part() {
		getOverlay().addListener(clickForRemoveOverlayListener);
		getLevel().getView().getPuzzleActor()
				.addModificationListener(clearAllTilesListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"This worked...\nBut it is much more efficient to clear all connections at once by double-tapping one of the tiles.\nLet's try",
				getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	private void intro4Part() {
		getOverlay().addListener(clickForRemoveOverlayListener);
		float width = getLevel().getView().getStage().getWidth();

		// Setup overlay content
		Label introText = new Label(
				"Perfect!\nNow solve the level on your own.", getSkin());
		introText.setAlignment(Align.center);
		introText.setWrap(true);
		introText.setFontScale(.7f);
		getContent().add(introText).width(width * .9f).row();
		getContent().add(continueHint);
	}

	@Override
	protected void start() {
		super.start();
		String continueHintString = Gdx.app.getType() == ApplicationType.Android ? "(tap to proceed)"
				: "(click to proceed)";
		continueHint = new Label(continueHintString, getSkin());
		continueHint.setFontScale(.5f);

		Puzzle p = getLevel().getPuzzle();
		p.getTile(0, 0).connectNeighbor(Direction.RIGHT, false);
		p.getTile(0, 1).connectNeighbor(Direction.RIGHT, false);
		p.getTile(0, 2).connectNeighbor(Direction.RIGHT, false);
		p.getTile(0, 3).connectNeighbor(Direction.RIGHT, false);
	}

	@Override
	protected void end() {
		getOverlay().removeListener(clickForNextListener);
		getOverlay().removeListener(clickForRemoveOverlayListener);
		getLevel().getPuzzle().removeChangeListener(puzzleChangedListener);
		super.end();
	}
}