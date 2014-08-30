package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.graphics.Color;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.ui.screens.TransitionableScreen;

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
		this.game = game;
		timePassed = 0;
		finished = false;
		this.fromScreen = fromScreen;
		this.toScreen = toScreen;
		ts = TransitionScreen.getInstance();
		Color fromClearColor = fromScreen.getClearColor();
		ts.setClearColor(fromClearColor.r, fromClearColor.g, fromClearColor.b,
				fromClearColor.a);
		ts.setup(this, fromScreen, getInitialFromScreenX(),
				getInitialFromScreenY(), toScreen, getInitialToScreenX(),
				getInitialToScreenY());
		setupDecorated();
		game.setScreen(ts);
		fromScreen.show();
		toScreen.show();
	}

	private void setupDecorated() {
		decorated.fromScreen = fromScreen;
		decorated.toScreen = toScreen;
		decorated.duration = duration;
		decorated.game = game;
		decorated.ts = ts;
	}

	@Override
	public void setDuration(float duration) {
		this.duration = duration;
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
			onEnd();
			if (decorated != null) {
				decorated.onEnd();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void onEnd() {
		game.setScreen(toScreen);
		Color toClearColor = toScreen.getClearColor();
		ts.setClearColor(toClearColor.r, toClearColor.g, toClearColor.b,
				toClearColor.a);
		ts.finish();
		finished = true;
		fromScreen.hide();
		fromScreen = null;
		toScreen = null;
		game = null;
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
