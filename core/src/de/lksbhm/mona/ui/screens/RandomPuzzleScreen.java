package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.puzzle.Puzzle;

public class RandomPuzzleScreen extends AbstractPuzzleScreen {

	private final PuzzleScreenState state = new PuzzleScreenState();
	private Label seedLabel;
	private long seed;
	private final InputAdapter backButtonHandler = new BackButtonToMainMenuHandler();
	private boolean disposePuzzle = false;

	public RandomPuzzleScreen() {
		setClearColor(0.1f, 0.1f, 0.1f, 1f);
	}

	@Override
	protected void onShow() {
		super.onShow();
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(Gdx.input.getInputProcessor());
		mux.addProcessor(backButtonHandler);
		Gdx.input.setInputProcessor(mux);
	}

	@Override
	protected void onWin() {
		super.onWin();
		disposePuzzle = true;
		Router router = LksBhmGame.getGame().getRouter();
		Transition transition = TransitionBuilder.buildNew().slideInRight()
				.interpolateClearColor().duration(.6f).get();
		router.changeScreen(GameWonScreen.class, null, transition);
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();
		seedLabel = new Label("" + seed, LksBhmGame.getGame().getDefaultSkin());
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
		super.setState(state);
		this.state.set((PuzzleScreenState) state);
		applyState();
	}

	private void applyState() {
	}

	@Override
	public Object getState() {
		state.set((AbstractPuzzleScreenState) super.getState());
		return state;
	}

	@Override
	protected void layoutWidgets() {
		seedLabel.setFontScale(0.3f);

		Table base = getBaseTable();
		base.clear();
		base.add(getPuzzleActor()).height(getDefaultViewportHeight() * 0.9f)
				.row();
		base.add(seedLabel);
	}

	@Override
	public void setPuzzle(Puzzle p) {
		state.p = p;
		super.setPuzzle(p);
	}

	private static class PuzzleScreenState extends AbstractPuzzleScreenState {
	}

	public void setSeed(long seed) {
		this.seed = seed;
		seedLabel.setText("" + seed);
	}

	@Override
	protected void onHide() {
		super.onHide();
		if (disposePuzzle) {
			state.p.dispose();
			state.p = null;
			super.setState(state);
			disposePuzzle = false;
		}
	}
}
