package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.graphics.Color;

import de.lksbhm.gdx.LksBhmGame;

abstract class AbstractTransition implements Transition {

	private TransitionScreen ts;
	private float duration;
	private float timePassed;
	private boolean finished;
	private TransitionableScreen fromScreen;
	private TransitionableScreen toScreen;
	private LksBhmGame game;
	private AbstractTransition decorated = null;

	@SuppressWarnings("deprecation")
	@Override
	public void apply(LksBhmGame game, TransitionableScreen fromScreen,
			TransitionableScreen toScreen) {
		setup(game, fromScreen, toScreen);

		Color fromClearColor = fromScreen.getClearColor();
		ts.setClearColor(fromClearColor.r, fromClearColor.g, fromClearColor.b,
				fromClearColor.a);
		ts.setup(this, this.fromScreen, getInitialFromScreenX(),
				getInitialFromScreenY(), this.toScreen, getInitialToScreenX(),
				getInitialToScreenY());
		this.fromScreen.disableHide();
		this.game.setScreen(ts);
		this.toScreen.show();
	}

	private void setup(LksBhmGame game, TransitionableScreen fromScreen,
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

	protected TransitionableScreen getFromScreen() {
		return fromScreen;
	}

	protected TransitionableScreen getToScreen() {
		return toScreen;
	}

	/**
	 * 
	 * @param delta
	 * @return true if the transition has ended
	 */
	void beforeRender(float delta) {
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

	void afterRender() {
		if (finished) {
			tearDown();
			if (decorated != null) {
				decorated.tearDownAsDecorated();
			}
		}
	}

	private void tearDownAsDecorated() {
		finished = true;
		fromScreen = null;
		toScreen = null;
		game = null;
	}

	@SuppressWarnings("deprecation")
	private void tearDown() {
		toScreen.disableShow();
		game.setScreen(toScreen);
		toScreen.enableShow();
		Color toClearColor = toScreen.getClearColor();
		ts.setClearColor(toClearColor.r, toClearColor.g, toClearColor.b,
				toClearColor.a);
		ts.finish();
		fromScreen.enableHide();
		fromScreen.hide();
		fromScreen.getStage().getRoot().setX(0);
		fromScreen.getStage().getRoot().setY(0);
		tearDownAsDecorated();
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
}
