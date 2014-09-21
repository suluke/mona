package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;

public class FadeOutFadeIn extends AbstractTransition {
	private final Color colorTo = new Color();
	private final Color colorFrom = new Color();
	private Group fromRoot;
	private Group toRoot;
	private Pool<FadeOutFadeIn> pool;

	@Override
	protected void update(TransitionScreen ts, float progress) {
		progress *= 2;
		if (progress < 1) {
			fromRoot.setColor(colorFrom.r, colorFrom.g, colorFrom.b,
					colorFrom.a * (1 - progress));
		} else {
			progress -= 1;
			toRoot.setColor(colorTo.r, colorTo.g, colorTo.b, colorTo.a
					* progress);
		}
	}

	@Override
	protected void onStart() {
		fromRoot = getFromScreen().getStage().getRoot();
		toRoot = getToScreen().getStage().getRoot();
		colorFrom.set(fromRoot.getColor());
		colorTo.set(toRoot.getColor());
		toRoot.setColor(colorTo.r, colorTo.g, colorTo.b, 0);
	}

	@Override
	protected void onEnd() {
		fromRoot.setColor(colorFrom);
		toRoot.setColor(colorTo);
	}

	public void setPool(Pool<FadeOutFadeIn> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("CancelOnTap", "Leaking transition");
		}
	}

}
