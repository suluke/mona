package de.lksbhm.gdx.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;

public abstract class AbstractScreen implements ResettableConsumerScreen {
	private final Stage stage;
	private final Table table;
	private Texture background = null;
	private final Viewport viewport;
	private final LksBhmGame game;

	public AbstractScreen() {
		this(1024, 600);
	}

	public AbstractScreen(int defaultWidth, int defaultHeight) {
		game = LksBhmGame.getGame();
		viewport = new ScalingViewport(Scaling.fit, defaultWidth, defaultHeight);
		stage = new Stage(viewport);

		table = new Table();
		table.setFillParent(true);
		if (game.isDebug()) {
			table.debug();
		}
		stage.addActor(table);
	}

	protected Table getBaseTable() {
		return table;
	}

	protected Viewport getViewport() {
		return viewport;
	}

	protected LksBhmGame getGame() {
		return game;
	}

	public void setBackground(Texture background) {
		this.background = background;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Batch batch = stage.getBatch();
		// begin a new batch and draw the background
		if (background != null) {
			batch.begin();
			batch.draw(background, 0, 0, (int) viewport.getWorldWidth(),
					(int) viewport.getWorldHeight());
			batch.end();
		}

		stage.act(delta);
		stage.draw();
		if (game.isDebug()) {
			stage.setDebugAll(true);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public final void dispose() {
		stage.dispose();
		if (background != null) {
			background.dispose();
		}
	}

	@Override
	public final void show() {
		Gdx.input.setInputProcessor(stage);
		onShow();
	}

	protected abstract void onShow();

	protected abstract void onDispose();

}
