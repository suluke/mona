package de.lksbhm.mona;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.gdx.util.GregorianCalendarValue;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.gdx.util.Version;
import de.lksbhm.gdx.util.Version.Status;
import de.lksbhm.gdx.util.Version.Visibility;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.ui.screens.LoadingScreen;
import de.lksbhm.mona.ui.screens.SplashScreen;

public class Mona extends LksBhmGame<Mona, User, MonaPlatform> {

	private LoadingScreen loadingScreen;
	private final Settings settings = new Settings();
	private final LevelPackageManager packageManager = new LevelPackageManager();
	private final DropInBehavior dropinBehavior = new DropInBehavior();
	private final Version version = new Version("", 0, 0, 4, Status.ALPHA,
			Visibility.INTERNAL, new GregorianCalendarValue(2014, 10, 11,
					1412979361 * 1000L));
	private final Skin skin = new Skin();

	public Mona() {
		super(User.instantiator);
	}

	@Override
	protected void initialize() {
		DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
		if (Gdx.app.getType() == ApplicationType.WebGL
				&& displayModes.length > 0) {
			Gdx.graphics.setDisplayMode(Gdx.graphics.getDisplayModes()[0]);
		}
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
			loadingScreen.setup(loadable, callback, 0, .6f);
			setScreen(loadingScreen);
		}
	}

	@Override
	protected Class<? extends TransitionableResettableConsumerScreen> getFirstScreen() {
		return SplashScreen.class;
	}

	@Override
	public void setScreen(TransitionableResettableConsumerScreen screen) {
		super.setScreen(screen);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
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

	@Override
	protected void registerDefaultSkinForLoad(AssetManager assetManager) {
		TextureAtlasParameter atlasParam = new TextureAtlasParameter();
		assetManager
				.load("textures/main.atlas", TextureAtlas.class, atlasParam);
	}

	@Override
	protected Skin getDefaultSkinAfterLoad(AssetManager assetManager) {
		TextureAtlas atlas = assetManager.get("textures/main.atlas");
		skin.addRegions(atlas);
		skin.load(Gdx.files.internal("json/skins/"
				+ getUserManager().getCurrentUser().getPaletteName() + ".json"));
		skin.load(Gdx.files.internal("json/skins/default.json"));
		return skin;
	}

	@Override
	public boolean drawDebug() {
		return false;
	}

	public static Mona getGame() {
		return (Mona) LksBhmGame.getGame();
	}
}
