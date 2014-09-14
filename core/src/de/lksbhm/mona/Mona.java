package de.lksbhm.mona;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.ui.screens.SplashScreen;
import de.lksbhm.mona.ui.screens.LoadingScreen;

public class Mona extends LksBhmGame<Mona, User> {

	private LoadingScreen loadingScreen;
	private final Settings settings = new Settings();
	private final LevelPackageManager packageManager = new LevelPackageManager();
	private final DropInBehavior dropinBehavior = new DropInBehavior();

	public Mona() {
		super(Mona.class, User.class);
	}

	@Override
	protected void initialize() {
		Gdx.input.setCatchBackKey(true);
		loadingScreen = new LoadingScreen();
		UserManager<User> userManager = getUserManager();
		if (userManager.getCurrentUser() == null) {
			// First start ever O.O, no?
			userManager.setCurrentUser(userManager.createUser());
		}
		if (!dropinBehavior.isLoaded()) {
			dropinBehavior.load();
		}
	};

	@Override
	protected void requestResources(AssetManager manager) {

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
		return SplashScreen.class;
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
}
