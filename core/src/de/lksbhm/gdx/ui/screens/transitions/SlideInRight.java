package de.lksbhm.gdx.ui.screens.transitions;

public class SlideInRight extends AbstractTransition {

	@Override
	public void update(TransitionScreen ts, float progress) {
		float x1 = -getFromScreen().getStage().getWidth() * progress;
		float x2 = x1 + getFromScreen().getStage().getWidth();
		ts.setX1(x1);
		ts.setX2(x2);
	}

	@Override
	protected float getInitialToScreenX() {
		return getFromScreen().getStage().getWidth();
	}
}
