package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.PuzzleWonListener;
import de.lksbhm.mona.ui.actors.PuzzleActor;

public abstract class AbstractPuzzleScreen extends AbstractScreen {
	private PuzzleActor puzzleActor;
	private AbstractPuzzleScreenState state = new AbstractPuzzleScreenState();
	private final PuzzleWonListener winListener = new PuzzleWonListener() {
		@Override
		public void onWin() {
			if (state.p != null) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						state.p.removeWinListener(winListener);
						AbstractPuzzleScreen.this.onWin();
					}
				});
			}
		}
	};
	private final TemporalAction increaseBrightness = new TemporalAction(0.6f) {

		@Override
		protected void update(float percent) {
			puzzleActor.setBrightnessIncrease(percent);
		}

		@Override
		protected void end() {
			puzzleActor.setBrightnessIncrease(0);
		};
	};

	public AbstractPuzzleScreen() {
		setClearColor(0.1f, 0.1f, 0.1f, 1f);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	@Override
	public void setState(Object state) {
		if (!(state instanceof AbstractPuzzleScreenState)) {
			throw new RuntimeException();
		}
		this.state = (AbstractPuzzleScreenState) state;
		applyState();
	}

	@Override
	public Object getState() {
		return state;
	}

	private void applyState() {
		if (state.p != null) {
			state.p.addWinListener(winListener);
		}
		puzzleActor.setPuzzle(state.p);
	}

	@Override
	protected void onShow() {
		layoutWidgets();
	}

	protected void onWin() {
		increaseBrightness.restart();
		puzzleActor.addAction(increaseBrightness);
	};

	protected void setupWidgets() {
		// TODO don't use default skin as it should be lightweight and without
		// custom widgets
		puzzleActor = new PuzzleActor(LksBhmGame.getGame().getDefaultSkin());
	}

	@Override
	protected void onHide() {
		puzzleActor.cancelInput();
	}

	protected void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		base.add(puzzleActor);
	}

	protected void setPuzzle(Puzzle p) {
		this.state.p = p;
		applyState();
	}

	protected PuzzleActor getPuzzleActor() {
		return puzzleActor;
	}

	protected static class AbstractPuzzleScreenState {
		Puzzle p;

		public void set(AbstractPuzzleScreenState other) {
			p = other.p;
		}
	}
}
