package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

class FadeClearColor extends AbstractTransition {

	private Pool<FadeClearColor> pool;

	@Override
	protected void update(TransitionScreen ts, float progress) {
		Color fromColor = getFromScreen().getClearColor();
		Color toColor = getToScreen().getClearColor();
		float r = toColor.r * progress + fromColor.r * (1 - progress);
		float g = toColor.g * progress + fromColor.g * (1 - progress);
		float b = toColor.b * progress + fromColor.b * (1 - progress);
		float a = toColor.a * progress + fromColor.a * (1 - progress);
		ts.setClearColor(r, g, b, a);
	}

	public void setPool(Pool<FadeClearColor> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("FadeClearColor", "Leaking transition");
		}
	}
}
