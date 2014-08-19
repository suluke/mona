package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.ui.screens.AbstractScreen;

public class MainMenuScreen extends AbstractScreen {

	private TextButton playButton;

	private void setupWidgets() {
		playButton = new TextButton("play", LksBhmGame.getGame()
				.getDefaultSkin(), "play");
		playButton.getLabel().setFontScale(.75f);
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		Viewport vp = getViewport();

		float w, h;
		w = vp.getWorldWidth() * 0.2f;
		h = vp.getWorldHeight() * 0.2f;
		base.add(playButton).size(w, h).center();
	}

	@Override
	public void onShow() {
		getBaseTable().clear();
		layoutWidgets();
	}

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
	public void onDispose() {
	}

	@Override
	public void requestResources(AssetManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return true;
	}

	@Override
	public long getEstimatedMemoryUsage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setState(Object state) {
		if (state != null) {
			if (state.getClass() != MainMenuState.class) {
				throw new IllegalArgumentException(
						"Given state not suitable for MainMenuScreens");
			}
		}
	}

	@Override
	public Object getState() {
		return new MainMenuState();
	}

	private static class MainMenuState {

	}
}