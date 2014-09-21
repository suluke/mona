package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

class SlideInRight extends AbstractTransition {

	private Pool<SlideInRight> pool = null;

	@Override
	public void update(TransitionScreen ts, float progress) {
		float x1 = -getFromScreen().getStage().getWidth() * progress;
		float x2 = x1 + getFromScreen().getStage().getWidth();
		ts.setFromX(x1);
		ts.setToX(x2);
	}

	@Override
	protected float getInitialToScreenX() {
		return getFromScreen().getStage().getWidth();
	}

	public void setPool(Pool<SlideInRight> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("SlideInRight", "Leaking transition");
		}
	}
}
