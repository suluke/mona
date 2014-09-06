package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {

	private static final float barWidth = .8f;
	private static final float barHeight = .2f;
	private static final float barVPos = .6f;

	private AssetManager assetManager;
	private Runnable onDoneCallback;
	private final NinePatch bar = new NinePatch(new Texture(
			"textures/loading.9.png"));;
	private final SpriteBatch batch = new SpriteBatch(1);
	private final Viewport vp = new ScalingViewport(Scaling.fit, 1024, 600);
	private float displayedProgress;
	private float currentProgress;

	public LoadingScreen() {
		batch.setProjectionMatrix(vp.getCamera().combined);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		currentProgress = assetManager.getProgress();

		displayedProgress = Interpolation.linear.apply(displayedProgress,
				currentProgress, 0.1f);

		float ww, wh, x, y, width, height;
		ww = vp.getWorldWidth();
		wh = vp.getWorldHeight();
		x = ww * (1 - barWidth) / 2;
		width = ww * barWidth * displayedProgress;
		y = wh * barVPos;
		height = wh * barHeight;

		batch.begin();
		bar.draw(batch, x, y, width, height);
		batch.end();
		if (assetManager.update()) {
			onDoneCallback.run();
		}
	}

	@Override
	public void resize(int width, int height) {
		vp.update(width, height);
	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		bar.getTexture().dispose();
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public void setOnDoneCallback(Runnable callback) {
		onDoneCallback = callback;
	}
}
