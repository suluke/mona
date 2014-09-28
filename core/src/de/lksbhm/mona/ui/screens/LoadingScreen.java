package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.mona.Mona;

public class LoadingScreen extends AbstractScreen {

	private static final float EPS = 1e-5f;

	private static final float barWidth = .8f;
	private static final float barHeight = .2f;
	private static final float barVPos = .2f;

	private Loadable<?> loadable;
	private Runnable onDoneCallback;
	private final NinePatch bar = new NinePatch(new Texture(
			"textures/loading.9.png"));
	private float displayedProgress;
	private float currentProgress;
	private boolean calledBack = false;
	private float delayLoading = 0;
	private float delayed = 0;
	private final float maxProgressPerFrame = .1f;

	public LoadingScreen() {
	}

	@Override
	public void render(float delta, boolean clear) {
		if (clear) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		}

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

		float ww, wh, x, y, width, height;
		ww = getViewport().getWorldWidth();
		wh = getViewport().getWorldHeight();
		x = ww * (1 - barWidth) / 2 + getStage().getRoot().getX();
		width = ww * barWidth * displayedProgress;
		y = wh * barVPos;
		height = wh * barHeight + getStage().getRoot().getY();

		Camera camera = getViewport().getCamera();
		camera.update();
		Batch batch = getStage().getBatch();

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		bar.draw(batch, x, y, width, height);
		batch.end();
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
