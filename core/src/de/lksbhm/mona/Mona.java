package de.lksbhm.mona;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.gdx.util.GregorianCalendarValue;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.gdx.util.Version;
import de.lksbhm.gdx.util.Version.Status;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.ui.screens.LoadingScreen;
import de.lksbhm.mona.ui.screens.SplashScreen;

public class Mona extends LksBhmGame<Mona, User, MonaPlatform> {

	private LoadingScreen loadingScreen;
	private final Settings settings = new Settings();
	private final LevelPackageManager packageManager = new LevelPackageManager();
	private final DropInBehavior dropinBehavior = new DropInBehavior();
	private final Version version = new Version("", 0, 0, 3, Status.ALPHA,
			new GregorianCalendarValue(2014, 10, 5, 1412461592L * 1000));

	public Mona() {
		super(User.instantiator);
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
	public void animateLoad(Loadable<?> loadable,
			Class<? extends ResourceConsumer> requester, Runnable callback) {
		if (requester == null) {
			// I decided to show no loading screen on app start
			loadable.finish();
			callback.run();
		} else {
			getRouter().saveCurrentScreenInHistory();
			loadingScreen.setup(loadable, callback, 0);
			setScreen(loadingScreen);
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

	@Override
	public Version getVersion() {
		return version;
	}
}
