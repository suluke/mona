package de.lksbhm.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.lksbhm.gdx.contexts.Context;
import de.lksbhm.gdx.contexts.ContextManager;
import de.lksbhm.gdx.platforms.AbstractPlatform;
import de.lksbhm.gdx.platforms.PlatformManager;
import de.lksbhm.gdx.resources.ResourceConsumer;
import de.lksbhm.gdx.resources.ResourceConsumerManager;
import de.lksbhm.gdx.ui.screens.TransitionableResettableConsumerScreen;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.gdx.util.Instantiator;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.gdx.util.LoadableAssetManager;
import de.lksbhm.gdx.util.Version;
import de.lksbhm.mona.User;

@SuppressWarnings("rawtypes")
public abstract class LksBhmGame<GameImplementation extends LksBhmGame, UserImplementation extends User, PlatformImplementation extends AbstractPlatform>
		extends Game implements Context {
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
	private final Instantiator<UserImplementation> userInstantiator;
	private UserManager<UserImplementation> userManager;

	private final PlatformManager<PlatformImplementation> platformManager = new PlatformManager<PlatformImplementation>();

	public LksBhmGame(Instantiator<UserImplementation> userInstantiator) {
		this("json/skin.json", "textures/main.atlas", userInstantiator);
	}

	public LksBhmGame(String defaultSkinPath, String defaultSkinAtlasPath,

	Instantiator<UserImplementation> userInstantiator) {
		this(defaultSkinPath, defaultSkinAtlasPath, 0, userInstantiator);
	}

	public LksBhmGame(String defaultSkinPath, String defaultSkinAtlasPath,
			int routerHistorySize,
			Instantiator<UserImplementation> userInstantiator) {
		instance = this;
		this.defaultSkinPath = defaultSkinPath;
		this.defaultSkinAtlasPath = defaultSkinAtlasPath;
		this.routerHistorySize = routerHistorySize;
		this.userInstantiator = userInstantiator;
	}

	@Override
	public final void create() {
		if (isDebug()) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_NONE);
		}
		router = new Router(this, routerHistorySize);
		userManager = new UserManager<UserImplementation>(userInstantiator);

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
		animateLoad(new LoadableAssetManager(assetManager), null,
				new Runnable() {
					@Override
					public void run() {
						defaultSkin = assetManager.get(defaultSkinPath);
						screen.onResourcesLoaded(assetManager);
						setScreen(screen);
						enterContext();
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

	@SuppressWarnings({ "unchecked" })
	public static <GameImplementation extends LksBhmGame> GameImplementation getGame(
			Class<GameImplementation> clazz) {
		return (GameImplementation) instance;
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

	public abstract void animateLoad(Loadable<?> loadable,
			Class<? extends ResourceConsumer> requester, Runnable callback);

	public ContextManager getContextManager() {
		return contextManager;
	}

	public UserManager<UserImplementation> getUserManager() {
		return userManager;
	}

	@Override
	public void enterContext() {
		contextManager.enterContext(this);
	}

	@Override
	public void leaveContext() {
		contextManager.leaveContext(this);
	}

	public PlatformManager<PlatformImplementation> getPlatformManager() {
		return this.platformManager;
	}

	public abstract Version getVersion();
}
