package de.lksbhm.gdx.ui.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Pool;

public class CancelOnTap extends AbstractTransition {

	private final InputProcessor listener = new InputProcessor() {

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return true;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			abort();
			return true;
		}

		@Override
		public boolean scrolled(int amount) {
			return true;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return true;
		}

		@Override
		public boolean keyUp(int keycode) {
			return true;
		}

		@Override
		public boolean keyTyped(char character) {
			abort();
			return true;
		}

		@Override
		public boolean keyDown(int keycode) {
			return true;
		}
	};

	private Pool<CancelOnTap> pool;

	public void setPool(Pool<CancelOnTap> pool) {
		this.pool = pool;
	}

	@Override
	public void dispose() {
		if (pool != null) {
			pool.free(this);
		} else {
			Gdx.app.error("CancelOnTap", "Leaking transition");
		}
	}

	@Override
	protected void update(TransitionScreen ts, float progress) {
	}

	@Override
	protected void onStart() {
		Gdx.input.setInputProcessor(listener);
	}

	@Override
	protected void onEnd() {
		Gdx.input.setInputProcessor(getToScreen().getInputProcessor());
	}

}
