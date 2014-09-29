package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.mona.Mona;

public class LoadingScreen extends AbstractScreen {

	private static final float EPS = 1e-5f;

	private static final float barWidth = .8f;
	private static final float barHeight = .02f;
	private static final float barVPos = .2f;

	private Loadable<?> loadable;
	private Runnable onDoneCallback;
	private final NinePatch bar = new NinePatch(new Texture(
			"textures/loading.9.png"));
	private final Image imageActor = new Image(bar);
	private float displayedProgress;
	private float currentProgress;
	private boolean calledBack = false;
	private float delayLoading = 0;
	private float delayed = 0;
	private final float maxProgressPerFrame = .1f;

	public LoadingScreen() {
		getStage().addActor(imageActor);
		float ww = getViewport().getWorldWidth();
		float wh = getViewport().getWorldHeight();
		float x = ww * (1 - barWidth) / 2 + getStage().getRoot().getX();
		float y = wh * barVPos;
		float height = wh * barHeight + getStage().getRoot().getY();
		imageActor.setBounds(x, y, 0, height);
	}

	@Override
	public void render(float delta, boolean clear) {
		if (delayed < delayLoading) {
			delayed += delta;
			return;
		}

		currentProgress = loadable.getProgress();

		displayedProgress += Math.min(currentProgress - displayedProgress,
				maxProgressPerFrame);
		if (currentProgress - displayedProgress < EPS) {
			displayedProgress = currentProgress;
		}

		float width = getViewport().getWorldWidth() * barWidth
				* displayedProgress;
		imageActor.setWidth(width);

		super.render(delta, clear);
		// The following code makes it easier to see what's going on in the
		// skin.json parsing process, especially when in gwt
		try {
			if (loadable.update() && displayedProgress == 1 && !calledBack) {
				calledBack = true;
				onDoneCallback.run();
			}
		} catch (Exception ex) {
			Throwable throwable = ex;
			while (throwable.getCause() != null) {
				throwable = throwable.getCause();
			}
			throw new RuntimeException("Error loading resources: "
					+ throwable.getMessage(), throwable);
		}
	}

	public void setup(Loadable<?> loadable, Runnable callback,
			float delayLoading) {
		this.loadable = loadable;
		this.onDoneCallback = callback;
		this.calledBack = false;
		this.delayLoading = delayLoading;
		delayed = 0;
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	public void setState(Object state) {
	}

	@Override
	public Object getState() {
		return null;
	}

	public static void showAsCurrentScreen(final Loadable<?> loadable,
			final Runnable callback, final Color clearColor,
			final float delayLoading, final Transition transition) {
		Mona mona = LksBhmGame.getGame(Mona.class);
		final Router router = mona.getRouter();
		router.obtainScreen(LoadingScreen.class,
				new ResourceConsumerObtainedCallback<LoadingScreen>() {
					@Override
					public void onObtained(LoadingScreen loadingScreen) {
						loadingScreen.setup(loadable, callback, delayLoading);
						loadingScreen.setClearColor(clearColor.r, clearColor.g,
								clearColor.b, clearColor.a);
						router.changeScreen(loadingScreen, transition);
					}
				});
	}
}
