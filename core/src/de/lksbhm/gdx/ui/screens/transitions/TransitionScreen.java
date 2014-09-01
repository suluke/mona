package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.lksbhm.gdx.ui.screens.TransitionableScreen;

public class TransitionScreen implements Screen {

	private static TransitionScreen instance;

	private TransitionableScreen fromScreen;
	private float x1;
	private float y1;
	private TransitionableScreen toScreen;
	private float x2;
	private float y2;
	private AbstractTransition transition;
	private final Color clearColor = new Color();

	private TransitionScreen() {
	}

	public void setup(AbstractTransition t, TransitionableScreen fromScreen,
			float x1, float y1, TransitionableScreen toScreen, float x2,
			float y2) {
		this.fromScreen = fromScreen;
		this.x1 = x1;
		this.y1 = y1;
		this.toScreen = toScreen;
		this.x2 = x2;
		this.y2 = y2;
		this.transition = t;
	}

	public TransitionableScreen getToScreen() {
		return toScreen;
	}

	/**
	 * 
	 * @return x1
	 */
	public float getX1() {
		return x1;
	}

	/**
	 * @param x1
	 */
	void setX1(float x1) {
		this.x1 = x1;
	}

	/**
	 * @return y1
	 */
	float getY1() {
		return y1;
	}

	/**
	 * @param y1
	 */
	void setY1(float y1) {
		this.y1 = y1;
	}

	/**
	 * @return x2
	 */
	float getX2() {
		return x2;
	}

	/**
	 * @param x2
	 */
	void setX2(float x2) {
		this.x2 = x2;
	}

	/**
	 * @return y2
	 */
	float getY2() {
		return y2;
	}

	/**
	 * @param y2
	 */
	void setY2(float y2) {
		this.y2 = y2;
	}

	public void finish() {
		fromScreen = null;
		toScreen = null;
	}

	public Color getClearColor() {
		return clearColor;
	};

	public void setClearColor(float r, float g, float b, float a) {
		clearColor.set(r, g, b, a);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b,
				clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		transition.beforeRender(delta);
		Group fromRoot = fromScreen.getStage().getRoot();
		fromRoot.setX(Math.round(x1));
		fromRoot.setY(Math.round(y1));
		Group toRoot = toScreen.getStage().getRoot();
		toRoot.setX(Math.round(x2));
		toRoot.setY(Math.round(y2));
		fromScreen.render(delta, false);
		toScreen.render(delta, false);
		transition.afterRender();
	}

	@Override
	public void resize(int width, int height) {
		fromScreen.resize(width, height);
		toScreen.resize(width, height);
	}

	@Override
	public void show() {
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
	}

	static TransitionScreen getInstance() {
		if (instance == null) {
			instance = new TransitionScreen();
		}
		return instance;
	}

}
