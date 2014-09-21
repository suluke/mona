package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.lksbhm.gdx.LksBhmGame;

abstract class AbstractTransition implements Transition {

	private final SharedTransitionProperties commonProperties = new SharedTransitionProperties();

	private AbstractTransition decorated = null;
	private AbstractTransition decorator = null;
	private boolean disposeOnFinish = false;

	@SuppressWarnings("deprecation")
	@Override
	public final void apply(LksBhmGame<?, ?> game,
			TransitionableScreen fromScreen, TransitionableScreen toScreen) {
		SharedTransitionProperties sharedProperties = getCommonProperties();
		sharedProperties.reset();
		sharedProperties.setGame(game);
		sharedProperties.setFromScreen(fromScreen);
		sharedProperties.setToScreen(toScreen);
		TransitionScreen ts = TransitionScreen.getInstance();
		sharedProperties.setTransitionScreen(ts);
		sharedProperties.setFinished(false);
		sharedProperties.setTimePassed(0);

		setupAll();

		Color fromClearColor = fromScreen.getClearColor();
		if (ts.getTransition() != null) {
			ts.getTransition().abort();
		}
		// In the beginning, we have the current clear color
		ts.setClearColor(fromClearColor.r, fromClearColor.g, fromClearColor.b,
				fromClearColor.a);
		ts.setup(this, fromScreen, commonProperties.initialFromScreenX,
				commonProperties.initialFromScreenY, toScreen,
				commonProperties.initialToScreenX,
				commonProperties.initialToScreenY);
		fromScreen.disableHide();
		game.setScreen(ts);
		toScreen.show();
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
		if (commonProperties.timePassed >= getDuration()) {
			commonProperties.timePassed = getDuration();
			commonProperties.finished = true;
		}
		TransitionScreen transitionScreen = getTransitionScreen();
		float progress = commonProperties.timePassed
				/ commonProperties.duration;
		if (decorated != null) {
			decorated.update(transitionScreen, progress);
		}
		update(transitionScreen, progress);

		commonProperties.timePassed += delta;
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
		SharedTransitionProperties commonProperties = getCommonProperties();
		commonProperties.setFinished(true);
		tearDownAsDecorated();
		commonProperties.setFromScreen(null);
		commonProperties.setToScreen(null);
		commonProperties.setGame(null);

	}

	protected SharedTransitionProperties getCommonProperties() {
		if (decorator != null) {
			return decorator.getCommonProperties();
		} else {
			return commonProperties;
		}
	}

	private LksBhmGame<?, ?> getGame() {
		return getCommonProperties().getGame();
	}

	private TransitionScreen getTransitionScreen() {
		return getCommonProperties().getTransitionScreen();
	}

	private boolean isFinished() {
		return getCommonProperties().isFinished();
	}

	protected TransitionableScreen getFromScreen() {
		return getCommonProperties().getFromScreen();
	}

	protected TransitionableScreen getToScreen() {
		return getCommonProperties().getToScreen();
	}

	@Override
	public void setDuration(float duration) {
		getCommonProperties().setDuration(duration);
	}

	@Override
	public float getDuration() {
		return getCommonProperties().getDuration();
	}

	public void runParallel(AbstractTransition transition) {
		this.decorated = transition;
		transition.decorator = this;
		getCommonProperties().mergeProperties(transition.getCommonProperties());
	}

	@Override
	public void setDisposeOnFinish(boolean b) {
		disposeOnFinish = b;
	}

	protected final TransitionableScreen getScreenRenderedBelow() {
		if (getCommonProperties().invertedDrawOrder) {
			return getFromScreen();
		} else {
			return getToScreen();
		}
	}

	protected final TransitionableScreen getScreenRenderedAbove() {
		if (getCommonProperties().invertedDrawOrder) {
			return getToScreen();
		} else {
			return getFromScreen();
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
		decorated = null;
		decorator = null;
	}
}
