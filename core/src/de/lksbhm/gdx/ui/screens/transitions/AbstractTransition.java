package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.lksbhm.gdx.LksBhmGame;

abstract class AbstractTransition implements Transition {

	private TransitionScreen ts;
	private float duration;
	private float timePassed;
	private boolean finished;
	private TransitionableScreen fromScreen;
	private TransitionableScreen toScreen;
	private LksBhmGame<?, ?> game;
	private AbstractTransition decorated = null;
	private boolean disposeOnFinish = false;

	@SuppressWarnings("deprecation")
	@Override
	public final void apply(LksBhmGame<?, ?> game,
			TransitionableScreen fromScreen, TransitionableScreen toScreen) {
		setup(game, fromScreen, toScreen);

		Color fromClearColor = fromScreen.getClearColor();
		if (ts.getTransition() != null) {
			ts.getTransition().abort();
		}
		// In the beginning, we have the current clear color
		ts.setClearColor(fromClearColor.r, fromClearColor.g, fromClearColor.b,
				fromClearColor.a);
		ts.setup(this, this.fromScreen, getInitialFromScreenX(),
				getInitialFromScreenY(), this.toScreen, getInitialToScreenX(),
				getInitialToScreenY());
		this.fromScreen.disableHide();
		this.game.setScreen(ts);
		this.toScreen.show();
		start();
	}

	private void start() {
		if (decorated != null) {
			decorated.start();
		}
		onStart();
	}

	private void setup(LksBhmGame<?, ?> game, TransitionableScreen fromScreen,
			TransitionableScreen toScreen) {
		this.game = game;
		this.fromScreen = fromScreen;
		this.toScreen = toScreen;
		ts = TransitionScreen.getInstance();
		timePassed = 0;
		finished = false;
		if (decorated != null) {
			decorated.setup(game, fromScreen, toScreen);
		}
	}

	@Override
	public void setDuration(float duration) {
		this.duration = duration;
		if (decorated != null) {
			decorated.setDuration(duration);
		}
	}

	/**
	 * 
	 * @param delta
	 * @return true if the transition has ended
	 */
	final void beforeRender(float delta) {
		if (finished) {
			return;
		}
		if (timePassed >= duration) {
			timePassed = duration;
			finished = true;
		}
		float progress = timePassed / duration;
		if (decorated != null) {
			decorated.update(ts, progress);
		}
		update(ts, progress);

		timePassed += delta;
	}

	final void afterRender() {
		if (finished) {
			tearDown();
		}
	}

	private void tearDownAsDecorated() {
		if (decorated != null) {
			decorated.tearDownAsDecorated();
		}
		finished = true;
		onEnd();
		fromScreen = null;
		toScreen = null;
		game = null;
		if (disposeOnFinish) {
			dispose();
		}
	}

	@SuppressWarnings("deprecation")
	private void tearDown() {
		Color clearColor = toScreen.getClearColor();
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b,
				clearColor.a);

		toScreen.disableShow();
		game.setScreen(toScreen);
		toScreen.enableShow();

		ts.finish();
		fromScreen.enableHide();
		fromScreen.hide();
		fromScreen.getStage().getRoot().setX(0);
		fromScreen.getStage().getRoot().setY(0);
		tearDownAsDecorated();
	}

	@Override
	public void abort() {
		tearDown();
	}

	protected void onStart() {
	}

	protected void onEnd() {
	}

	/**
	 * 
	 * @param ts
	 * @param progress
	 *            value between 0 and 1 indicating how much of the transition
	 *            has passed
	 */
	protected abstract void update(TransitionScreen ts, float progress);

	public void runParallel(AbstractTransition transition) {
		this.decorated = transition;
	}

	@Override
	public void setDisposeOnFinish(boolean b) {
		disposeOnFinish = b;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	protected float getInitialFromScreenX() {
		return 0;
	}

	protected float getInitialFromScreenY() {
		return 0;
	}

	protected float getInitialToScreenX() {
		return 0;
	}

	protected float getInitialToScreenY() {
		return 0;
	}

	protected TransitionableScreen getScreenRenderedBelow() {
		return getFromScreen();
	}

	protected TransitionableScreen getScreenRenderedAbove() {
		return getToScreen();
	}

	protected TransitionableScreen getFromScreen() {
		return fromScreen;
	}

	protected TransitionableScreen getToScreen() {
		return toScreen;
	}

	@Override
	public void dispose() {
		if (decorated != null) {
			decorated.dispose();
		}
		decorated = null;
	}
}
