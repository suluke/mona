package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

public class SlideInLeft extends AbstractTransition {

	private Pool<SlideInLeft> pool = null;

	@Override
	public void dispose() {
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("SlideInLeft", "Leaking transition");
		}
	}

	@Override
	protected void update(TransitionScreen ts, float progress) {
		float fromX = getFromScreen().getStage().getWidth() * progress;
		float toX = fromX - getToScreen().getStage().getWidth();
		ts.setFromX(fromX);
		ts.setToX(toX);
	}

	public void setPool(Pool<SlideInLeft> pool) {
		this.pool = pool;
	}

	@Override
	protected float getInitialToScreenX() {
		return -getToScreen().getStage().getWidth();
	}
}
