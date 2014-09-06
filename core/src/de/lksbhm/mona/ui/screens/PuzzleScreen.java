package de.lksbhm.mona.ui.screens;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.puzzle.Puzzle;

public class PuzzleScreen extends AbstractPuzzleScreen {

	private final PuzzleScreenState state = new PuzzleScreenState();

	public PuzzleScreen() {
		setClearColor(0.1f, 0.1f, 0.1f, 1f);
	}

	@Override
	protected void onWin() {
		state.p.dispose();
		state.p = null;
		super.setState(state);
		Router router = LksBhmGame.getGame().getRouter();
		Transition transition = TransitionBuilder.buildNew().slideInRight()
				.interpolateClearColor().get();
		transition.setDuration(.6f);
		router.changeScreen(GameWonScreen.class, null, transition);
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();
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
		super.layoutWidgets();
	}

	@Override
	public void setPuzzle(Puzzle p) {
		state.p = p;
		super.setPuzzle(p);
	}

	private static class PuzzleScreenState extends AbstractPuzzleScreenState {
	}
}
