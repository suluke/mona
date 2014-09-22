package de.lksbhm.gdx.ui.screens.transitions;

import de.lksbhm.gdx.LksBhmGame;

/**
 * Containing all properties that only apply once even if a transition is
 * decorated. E.g., draw order of the screen is determined only by the topmost
 * Transition
 */
class SharedTransitionProperties {
	float timePassed;
	float duration;
	boolean finished;
	TransitionScreen ts;
	TransitionableScreen fromScreen;
	TransitionableScreen toScreen;
	boolean drawOrderInverted = false;
	LksBhmGame<?, ?> game;
	float initialFromScreenX = 0;
	float initialFromScreenY = 0;
	float initialToScreenX = 0;
	float initialToScreenY = 0;

	private boolean initialScreenPositionsDetermined = false;
	private boolean drawOrderDetermined = false;
	private boolean durationDetermined = false;

	public float getDuration() {
		return duration;
	}

	public LksBhmGame<?, ?> getGame() {
		return game;
	}

	public TransitionScreen getTransitionScreen() {
		return ts;
	}

	public void setFinished(boolean b) {
		finished = b;
	}

	public void setFromScreen(TransitionableScreen screen) {
		this.fromScreen = screen;
	}

	public TransitionableScreen getFromScreen() {
		return fromScreen;
	}

	public TransitionableScreen getToScreen() {
		return toScreen;
	}

	public void setGame(LksBhmGame<?, ?> game) {
		this.game = game;
	}

	public void setToScreen(TransitionableScreen screen) {
		this.toScreen = screen;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setTransitionScreen(TransitionScreen transitionScreen) {
		this.ts = transitionScreen;
	}

	public void setTimePassed(float timePassed) {
		this.timePassed = timePassed;
	}

	public void setDuration(float duration) {
		this.duration = duration;
		durationDetermined = true;
	}

	public void setDrawOrderInverted(boolean inverted) {
		if (drawOrderInverted != inverted) {
			if (drawOrderDetermined) {
				throw new RuntimeException("Mixing incompatible Transitions");
			}
			drawOrderInverted = inverted;
		}
		drawOrderDetermined = true;
	}

	public void setInitialScreenPositions(float initialFromScreenX,
			float initialFromScreenY, float initialToScreenX,
			float initialToScreenY) {
		if (this.initialFromScreenX != initialFromScreenX
				|| this.initialFromScreenY != initialFromScreenY
				|| this.initialToScreenX != initialToScreenX
				|| this.initialToScreenY != initialToScreenY) {
			if (initialScreenPositionsDetermined) {
				throw new RuntimeException("Mixing incompatible Transitions");
			}
			this.initialFromScreenX = initialFromScreenX;
			this.initialFromScreenY = initialFromScreenY;
			this.initialToScreenX = initialToScreenX;
			this.initialToScreenY = initialToScreenY;
		}
		initialScreenPositionsDetermined = true;
	}

	public void mergeProperties(SharedTransitionProperties properties) {
		if (properties.drawOrderDetermined) {
			setDrawOrderInverted(properties.drawOrderInverted);
		}
		if (properties.initialScreenPositionsDetermined) {
			setInitialScreenPositions(properties.initialFromScreenX,
					properties.initialFromScreenY, properties.initialToScreenX,
					properties.initialToScreenY);
		}
		if (properties.durationDetermined) {
			if (durationDetermined) {
				if (properties.duration != duration) {
					throw new RuntimeException(
							"You already set a duration for the cascaded transitions");
				}
			} else {
				duration = properties.duration;
			}
			durationDetermined = true;
		}
	}

	public void resetBeforeApply() {
		timePassed = 0;
		finished = false;
		ts = null;
		fromScreen = null;
		toScreen = null;
		drawOrderInverted = false;
		game = null;
		initialFromScreenX = 0;
		initialFromScreenY = 0;
		initialToScreenX = 0;
		initialToScreenY = 0;

		initialScreenPositionsDetermined = false;
		drawOrderDetermined = false;
	}

	public void resetOnDispose() {
		resetBeforeApply();
		duration = 0;
		durationDetermined = false;
	}
}
