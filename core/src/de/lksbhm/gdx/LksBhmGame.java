package de.lksbhm.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.lksbhm.gdx.contexts.ContextManager;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.resources.ResourceConsumerManager;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.users.User;
import de.lksbhm.gdx.users.UserManager;

public abstract class LksBhmGame extends Game {
	private static LksBhmGame instance;

	private final String defaultSkinPath;
	private final String defaultSkinAtlasPath;

	private final AssetManager assetManager = new AssetManager();
	private final ResourceConsumerManager resourceConsumerManager = new ResourceConsumerManager(
			this);
	private final ContextManager contextManager = new ContextManager();
	private Router router;
	private Skin defaultSkin;
	private int routerHistorySize = 0;
	private User currentUser;
	private final UserManager userManager = new UserManager();

	public LksBhmGame() {
		instance = this;
		defaultSkinPath = "json/skin.json";
		defaultSkinAtlasPath = "textures/main.atlas";
	}

	public LksBhmGame(String defaultSkinPath, String defaultSkinAtlasPath) {
		instance = this;
		this.defaultSkinPath = defaultSkinPath;
		this.defaultSkinAtlasPath = defaultSkinAtlasPath;
	}

	public LksBhmGame(String defaultSkinPath, String defaultSkinAtlasPath,
			int routerHistorySize) {
		this(defaultSkinPath, defaultSkinAtlasPath);
		this.routerHistorySize = routerHistorySize;
	}

	@Override
	public final void create() {
		if (isDebug()) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_NONE);
		}
		router = new Router(this, routerHistorySize);

		initialize();
		loadAndStart();
	}

	private void loadAndStart() {
		SkinParameter skinParam = new SkinParameter(defaultSkinAtlasPath);
		assetManager.load(defaultSkinPath, Skin.class, skinParam);
		final TransitionableResettableConsumerScreen screen = resourceConsumerManager
				.obtainConsumerInstanceWithoutLoadingResources(getFirstScreen());
		// don't need to have screen request resources as this is done in
		// obtainConsumerInstanceWithoutLoadingResources
		requestResources(assetManager);
		animateAssetManagerLoad(assetManager, null, new Runnable() {
			@Override
			public void run() {
				defaultSkin = assetManager.get(defaultSkinPath);
				screen.onResourcesLoaded(assetManager);
				setScreen(screen);
			}
		});
	}

	protected void initialize() {

	}

	protected abstract void requestResources(AssetManager manager);

	protected abstract Class<? extends TransitionableResettableConsumerScreen> getFirstScreen();

	public static LksBhmGame getGame() {
		return instance;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public ResourceConsumerManager getResourceConsumerManager() {
		return resourceConsumerManager;
	}

	public Router getRouter() {
		return router;
	}

	public Skin getDefaultSkin() {
		return defaultSkin;
	}

	@Override
	@Deprecated
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	public void setScreen(TransitionableResettableConsumerScreen screen) {
		super.setScreen(screen);
	}

	Screen getScreenRaw() {
		return super.getScreen();
	}

	@Override
	public TransitionableResettableConsumerScreen getScreen() {
		return (TransitionableResettableConsumerScreen) super.getScreen();
	}

	public boolean isDebug() {
		return false;
	}

	public abstract Settings getSettings();

	public abstract void animateAssetManagerLoad(AssetManager manager,
			Class<? extends ResourceConsumer> requester, Runnable callback);

	public ContextManager getContextManager() {
		return contextManager;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	protected void setCurrentUser(User user) {
		this.currentUser = user;
	}

	public UserManager getUserManager() {
		return userManager;
	}
}
