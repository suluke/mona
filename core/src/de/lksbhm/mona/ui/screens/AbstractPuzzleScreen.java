package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.ui.actors.PuzzleActor;

public abstract class AbstractPuzzleScreen extends AbstractScreen {
	private PuzzleActor puzzle;
	private AbstractPuzzleScreenState state = new AbstractPuzzleScreenState();
	private final PuzzleChangedListener winListener = new PuzzleChangedListener() {
		@Override
		public void onChange() {
			if (state.p != null && state.p.isSolved()) {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						state.p.removeChangeListener(winListener);
						onWin();
					}
				});
			}
		}
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
			state.p.addChangeListener(winListener);
			puzzle.setPuzzle(state.p);
		}
	}

	@Override
	protected void onShow() {
		layoutWidgets();
	}

	protected void onWin() {
	};

	protected void setupWidgets() {
		// TODO don't use default skin as it should be lightweight and without
		// custom widgets
		puzzle = new PuzzleActor(LksBhmGame.getGame().getDefaultSkin());
	}

	@Override
	protected void onHide() {
		puzzle.cancelInput();
	}

	protected void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		base.add(puzzle);
	}

	protected void setPuzzle(Puzzle p) {
		this.state.p = p;
		applyState();
	}

	protected PuzzleActor getPuzzleActor() {
		return puzzle;
	}

	protected static class AbstractPuzzleScreenState {
		Puzzle p;

		public void set(AbstractPuzzleScreenState other) {
			p = other.p;
		}
	}
}
