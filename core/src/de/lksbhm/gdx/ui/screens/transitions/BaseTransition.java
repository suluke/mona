package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

/**
 * This transition does nothing and is only intended to help the
 * {@link TransitionBuilder} as a sentinel.
 *
 */
final class BaseTransition extends AbstractTransition {

	private Pool<BaseTransition> pool;

	@Override
	protected void update(TransitionScreen ts, float progress) {
	}

	public void setPool(Pool<BaseTransition> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("BaseTransition", "Leaking transition");
		}
	}

}
