package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.ui.screens.AbstractScreen;
import de.lksbhm.mona.ui.actors.PuzzleActor;

public class PuzzleScreen extends AbstractScreen {

	private PuzzleActor puzzle;

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
		// TODO Auto-generated method stub

	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	protected void onDispose() {
		// TODO Auto-generated method stub

	}
}
