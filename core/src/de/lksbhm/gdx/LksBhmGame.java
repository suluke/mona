package de.lksbhm.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.resources.ResourceConsumerManager;
import de.lksbhm.gdx.ui.screens.ResettableConsumerScreen;

public abstract class LksBhmGame extends Game {
	private static LksBhmGame instance;

	private final String defaultSkinPath;
	private final String defaultSkinAtlasPath;

	private AssetManager assetManager;
	private ResourceConsumerManager resourceConsumerManager;
	private Router router;
	private Skin defaultSkin;
	private int routerHistorySize = 0;

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
		assetManager = new AssetManager();
		resourceConsumerManager = new ResourceConsumerManager(this);
		router = new Router(this, routerHistorySize);

		initialize();

		SkinParameter skinParam = new SkinParameter(defaultSkinAtlasPath);
		assetManager.load(defaultSkinPath, Skin.class, skinParam);
		ResettableConsumerScreen screen = resourceConsumerManager
				.obtainConsumerInstance(getFirstScreen(), false);
		requestResources(assetManager);
		animateAssetManagerLoad(assetManager, null);
		defaultSkin = assetManager.get(defaultSkinPath);
		screen.onResourcesLoaded(assetManager);
		setScreen(screen);
	}

	protected void initialize() {

	}

	protected abstract void requestResources(AssetManager manager);

	protected abstract Class<? extends ResettableConsumerScreen> getFirstScreen();

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
		// TODO Auto-generated method stub
		super.setScreen(screen);
	}

	public void setScreen(ResettableConsumerScreen screen) {
		super.setScreen(screen);
	}

	@Override
	public ResettableConsumerScreen getScreen() {
		return (ResettableConsumerScreen) super.getScreen();
	}

	public boolean isDebug() {
		return false;
	}

	public abstract Settings getSettings();

	public abstract void animateAssetManagerLoad(AssetManager manager,
			Class<? extends ResourceConsumer> requester);
}
