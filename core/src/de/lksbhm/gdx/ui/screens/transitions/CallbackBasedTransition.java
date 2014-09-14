package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

public class CallbackBasedTransition extends AbstractTransition {

	public static interface Callback {
		void update(float progress);
	}

	private Pool<CallbackBasedTransition> pool = null;
	private Callback callback;

	CallbackBasedTransition() {
	}

	public CallbackBasedTransition(Callback callback) {
		this.callback = callback;
	}

	void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void dispose() {
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("CallbackBasedTransition", "Leaking transition");
		}
	}

	@Override
	protected void update(TransitionScreen ts, float progress) {
		callback.update(progress);
	}

	@Override
	protected TransitionableScreen getScreenRenderedBelow() {
		// We assume that the current screen does something to make the
		// UNDERLYING toScreen visible.
		return getToScreen();
	}

	@Override
	protected TransitionableScreen getScreenRenderedAbove() {
		return getFromScreen();
	}

	public void setPool(Pool<CallbackBasedTransition> pool) {
		this.pool = pool;
	}
}
