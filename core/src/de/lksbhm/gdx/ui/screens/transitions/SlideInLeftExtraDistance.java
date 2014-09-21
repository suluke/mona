package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

public class SlideInLeftExtraDistance extends AbstractTransition {

	private Pool<SlideInLeftExtraDistance> pool;

	@Override
	protected void update(TransitionScreen ts, float progress) {
		float width = getFromScreen().getStage().getWidth();
		float fromX = 2 * width * progress;
		float toX = -2 * width * (1 - progress);
		ts.setFromX(fromX);
		ts.setToX(toX);
	}

	@Override
	protected void setup() {
		getSharedProperties().setInitialScreenPositions(0, 0,
				-2 * getToScreen().getStage().getWidth(), 0);
	}

	public void setPool(Pool<SlideInLeftExtraDistance> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("ExtraDistanceSlideInLeft", "Leaking transition");
		}
	}
}
