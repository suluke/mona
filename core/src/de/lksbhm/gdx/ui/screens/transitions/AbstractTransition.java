package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.lksbhm.gdx.LksBhmGame;

abstract class AbstractTransition implements Transition {

	private final SharedTransitionProperties sharedProperties = new SharedTransitionProperties();

	private AbstractTransition decorated = null;
	private AbstractTransition decorator = null;
	private boolean disposeOnFinish = false;

	@SuppressWarnings("deprecation")
	@Override
	public final void apply(LksBhmGame<?, ?, ?> game,
			TransitionableScreen fromScreen, TransitionableScreen toScreen) {
		if (fromScreen == null) {
			throw new IllegalArgumentException("FromScreen must not be null");
		}
		if (toScreen == null) {
			throw new IllegalArgumentException("ToScreen must not be null");
		}
		if (fromScreen == toScreen) {
			throw new IllegalArgumentException(
					"Don't try transitioning between one and the same screen");
		}
		if (getSharedProperties().isRunning()) {
			throw new IllegalStateException(
					"Are you trying to use this transition twice at the same time?");
		}
		SharedTransitionProperties sharedProperties = getSharedProperties();
		sharedProperties.resetBeforeApply();
		sharedProperties.setGame(game);
		sharedProperties.setFromScreen(fromScreen);
		sharedProperties.setToScreen(toScreen);
		TransitionScreen ts = TransitionScreen.getInstance();
		sharedProperties.setTransitionScreen(ts);
		sharedProperties.setFinished(false);
		sharedProperties.setTimePassed(0);

		setupAll();

		if (ts.getTransition() != null) {
			ts.getTransition().abort();
		}
		// In the beginning, we have the current clear color
		Color fromClearColor = fromScreen.getClearColor();
		ts.setClearColor(fromClearColor.r, fromClearColor.g, fromClearColor.b,
				fromClearColor.a);
		ts.setup(this, fromScreen, sharedProperties.initialFromScreenX,
				sharedProperties.initialFromScreenY, toScreen,
				sharedProperties.initialToScreenX,
				sharedProperties.initialToScreenY);
		fromScreen.disableHide();
		game.setScreen(ts);
		toScreen.show();
		// There will be one more frame, so this is necessary, since .show()
		// sets the clear color differently
		Gdx.gl.glClearColor(fromClearColor.r, fromClearColor.g,
				fromClearColor.b, fromClearColor.a);
		getSharedProperties().setRunning(true);
		start();
	}

	private void setupAll() {
		if (decorated != null) {
			decorated.setup();
		}
		setup();
	}

	private void start() {
		if (decorated != null) {
			decorated.start();
		}
		onStart();
	}

	/**
	 * Top-level-only method
	 * 
	 * @param delta
	 * @return true if the transition has ended
	 */
	final void beforeRender(float delta) {
		if (isFinished()) {
			return;
		}
		if (sharedProperties.timePassed >= getDuration()) {
			sharedProperties.timePassed = getDuration();
			sharedProperties.finished = true;
		}
		TransitionScreen transitionScreen = getTransitionScreen();
		float progress = sharedProperties.timePassed
				/ sharedProperties.duration;
		if (decorated != null) {
			decorated.update(transitionScreen, progress);
		}
		update(transitionScreen, progress);

		sharedProperties.timePassed += delta;
	}

	final void afterRender() {
		if (isFinished()) {
			tearDown();
		}
	}

	private void tearDownAsDecorated() {
		if (decorated != null) {
			decorated.tearDownAsDecorated();
		}
		onEnd();
		if (disposeOnFinish) {
			dispose();
		}
	}

	@SuppressWarnings("deprecation")
	private void tearDown() {
		TransitionableScreen toScreen = getToScreen();
		TransitionableScreen fromScreen = getFromScreen();

		Color clearColor = toScreen.getClearColor();
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b,
				clearColor.a);

		toScreen.disableShow();
		getGame().setScreen(toScreen);
		toScreen.enableShow();

		getTransitionScreen().finish();
		fromScreen.enableHide();
		fromScreen.hide();
		fromScreen.getStage().getRoot().setX(0);
		fromScreen.getStage().getRoot().setY(0);
		SharedTransitionProperties commonProperties = getSharedProperties();
		commonProperties.setFinished(true);
		tearDownAsDecorated();
		commonProperties.setFromScreen(null);
		commonProperties.setToScreen(null);
		commonProperties.setGame(null);
		getSharedProperties().setRunning(false);
	}

	protected SharedTransitionProperties getSharedProperties() {
		if (decorator != null) {
			return decorator.getSharedProperties();
		} else {
			return sharedProperties;
		}
	}

	private LksBhmGame<?, ?, ?> getGame() {
		return getSharedProperties().getGame();
	}

	private TransitionScreen getTransitionScreen() {
		return getSharedProperties().getTransitionScreen();
	}

	private boolean isFinished() {
		return getSharedProperties().isFinished();
	}

	protected TransitionableScreen getFromScreen() {
		return getSharedProperties().getFromScreen();
	}

	protected TransitionableScreen getToScreen() {
		return getSharedProperties().getToScreen();
	}

	@Override
	public void setDuration(float duration) {
		getSharedProperties().setDuration(duration);
	}

	@Override
	public float getDuration() {
		return getSharedProperties().getDuration();
	}

	public void runParallel(AbstractTransition transition) {
		getSharedProperties().mergeProperties(transition.getSharedProperties());
		this.decorated = transition;
		transition.decorator = this;
	}

	@Override
	public void setDisposeOnFinish(boolean b) {
		disposeOnFinish = b;
	}

	/**
	 * Sets disposeOnFinish for this transition and all decorated transitions.
	 * 
	 * @param b
	 */
	public void setDisposeOnFinishRecursive(boolean b) {
		if (decorated != null) {
			decorated.setDisposeOnFinishRecursive(b);
		}
		disposeOnFinish = b;
	}

	protected final TransitionableScreen getScreenRenderedBelow() {
		if (getSharedProperties().drawOrderInverted) {
			return getToScreen();
		} else {
			return getFromScreen();
		}
	}

	protected final TransitionableScreen getScreenRenderedAbove() {
		if (getSharedProperties().drawOrderInverted) {
			return getFromScreen();
		} else {
			return getToScreen();
		}
	}

	/*
	 * Stuff you may want to override
	 */
	@Override
	public void abort() {
		tearDown();
	}

	protected void setup() {

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

	@Override
	public void dispose() {
		sharedProperties.resetOnDispose();
		decorated = null;
		decorator = null;
	}
}
