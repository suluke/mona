package de.lksbhm.mona;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.resources.ResourceConsumerManager;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.ui.screens.LoadingScreen;
import de.lksbhm.mona.ui.screens.MainMenuScreen;

public class Mona extends LksBhmGame {

	private LoadingScreen loadingScreen;
	private final Settings settings = new Settings();
	private final LevelPackageManager packageManager = new LevelPackageManager();
	private final DropInBehavior dropinBehavior = new DropInBehavior();

	public Mona() {
		// TODO lucky this works...
		dropinBehavior.load();
	}

	@Override
	protected void initialize() {
		Gdx.input.setCatchBackKey(true);
		loadingScreen = new LoadingScreen();
	};

	@Override
	protected void requestResources(AssetManager manager) {
		ResourceConsumerManager consumerManager = getResourceConsumerManager();
		// automatically handled by LksBhmGame:
		consumerManager
				.obtainConsumerInstanceWithoutLoadingResources(MainMenuScreen.class);
	}

	@Override
	public void animateAssetManagerLoad(AssetManager manager,
			Class<? extends ResourceConsumer> requester, Runnable callback) {
		if (requester == null) {
			Gdx.app.log("Mona", "Showing loading screen");
			getRouter().saveCurrentScreenInHistory();
			loadingScreen.setAssetManager(manager);
			loadingScreen.setOnDoneCallback(callback);
			setScreen(loadingScreen);
		} else {
			Gdx.app.log("Mona", requester.getSimpleName() + " finishLoading");
			manager.finishLoading();
			callback.run();
		}
	}

	@Override
	protected Class<? extends TransitionableResettableConsumerScreen> getFirstScreen() {
		return MainMenuScreen.class;
	}

	@Override
	public void setScreen(TransitionableResettableConsumerScreen screen) {
		if (screen != null) {
			Gdx.app.log(screen.getClass().getSimpleName(), "set screen");
		}
		super.setScreen(screen);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setScreen(Screen screen) {
		if (screen != null) {
			Gdx.app.log("setScreen", "deprecated function call");
			Gdx.app.log(screen.getClass().getSimpleName(), "set screen");
		}
		super.setScreen(screen);
	}

	@Override
	public boolean isDebug() {
		return false;
	}

	@Override
	public Settings getSettings() {
		return settings;
	}

	public LevelPackageManager getLevelPackageManager() {
		return packageManager;
	}

	@Override
	public User instantiateUserImplementation() {
		return new User();
	}
}
