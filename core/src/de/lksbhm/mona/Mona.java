package de.lksbhm.mona;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.resources.ResourceConsumerManager;
import de.lksbhm.gdx.ui.screens.ResettableConsumerScreen;
import de.lksbhm.mona.ui.screens.LoadingScreen;
import de.lksbhm.mona.ui.screens.MainMenuScreen;

public class Mona extends LksBhmGame {

	private LoadingScreen loadingScreen;
	private MainMenuScreen mainMenuScreen;

	@Override
	protected void initialize() {
		Gdx.graphics.getGL20().glClearColor(0.518f, 0.863f, 0.796f, 1f);
		loadingScreen = new LoadingScreen();
	};

	@Override
	protected void requestResources(AssetManager manager) {
		ResourceConsumerManager consumerManager = getResourceConsumerManager();
		mainMenuScreen = consumerManager.obtainConsumerInstance(
				MainMenuScreen.class, false);
	}

	@Override
	public void animateAssetManagerLoad(AssetManager manager,
			Class<? extends ResourceConsumer> requester) {
		if (requester == null) {
			getRouter().saveCurrentScreenInHistory();
			setScreen(loadingScreen);
			while (!manager.update()) {
				loadingScreen.update(manager.getProgress());
			}
			getRouter().resetPreviousScreenFromHistory();
		} else {
			Gdx.app.log("Mona", requester.getSimpleName() + " finishLoading");
			manager.finishLoading();
		}
	}

	@Override
	protected Class<? extends ResettableConsumerScreen> getFirstScreen() {
		return MainMenuScreen.class;
	}

	@Override
	public void setScreen(ResettableConsumerScreen screen) {
		Gdx.app.log(screen.getClass().getSimpleName(), "set screen");
		super.setScreen(screen);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setScreen(Screen screen) {
		Gdx.app.log("setScreen", "deprecated function call");
		Gdx.app.log(screen.getClass().getSimpleName(), "set screen");
		super.setScreen(screen);
	}

	@Override
	public boolean isDebug() {
		return true;
	}
}
