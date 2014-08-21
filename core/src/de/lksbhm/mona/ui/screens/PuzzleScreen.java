package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.ui.actors.PuzzleActor;

public class PuzzleScreen extends AbstractScreen {

	private PuzzleActor puzzle;
	private PuzzleScreenState state = new PuzzleScreenState();

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestResources(AssetManager manager) {
		// TODO Auto-generated method stub

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
	public long getEstimatedMemoryUsage() {
		return 0;
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
		puzzle.setPuzzle(state.p);
	}

	@Override
	public Object getState() {
		return state;
	}

	@Override
	protected void onShow() {
		Gdx.graphics.getGL20().glClearColor(0.1f, 0.1f, 0.1f, 1f);
		getBaseTable().clear();
		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.add(puzzle);
	}

	@Override
	protected void onDispose() {
		// TODO Auto-generated method stub

	}

	public void setPuzzle(Puzzle p) {
		this.state.p = p;
		applyState();
	}

	private static class PuzzleScreenState {
		Puzzle p;
	}
}
