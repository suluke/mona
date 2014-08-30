package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.InterpolateClearColor;
import de.lksbhm.gdx.ui.screens.transitions.SlideInRight;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.PuzzleChangedListener;
import de.lksbhm.mona.ui.actors.PuzzleActor;

public class PuzzleScreen extends AbstractScreen {

	private PuzzleActor puzzle;
	private PuzzleScreenState state = new PuzzleScreenState();
	private final PuzzleChangedListener winListener = new PuzzleChangedListener() {
		@Override
		public void onChange() {
			if (state.p.isSolved()) {
				state.p.dispose();
				state.p = null;
				Router router = LksBhmGame.getGame().getRouter();
				SlideInRight slide = new SlideInRight();
				InterpolateClearColor blendColors = new InterpolateClearColor();
				slide.runParallel(blendColors);
				slide.setDuration(.6f);
				router.changeScreen(GameWonScreen.class, null, slide);
			}
		}
	};

	public PuzzleScreen() {
		setClearColor(0.1f, 0.1f, 0.1f, 1f);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		// TODO don't use default skin as it should be lightweight and without
		// custom widgets
		puzzle = new PuzzleActor(LksBhmGame.getGame().getDefaultSkin());
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return true;
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != PuzzleScreenState.class) {
			throw new RuntimeException();
		}
		this.state = (PuzzleScreenState) state;
		applyState();
	}

	private void applyState() {
		state.p.addChangeListener(winListener);
		puzzle.setPuzzle(state.p);
	}

	@Override
	public Object getState() {
		return state;
	}

	@Override
	protected void onShow() {
		getBaseTable().clear();
		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.add(puzzle);
	}

	public void setPuzzle(Puzzle p) {
		this.state.p = p;
		applyState();
	}

	private static class PuzzleScreenState {
		Puzzle p;
	}
}
