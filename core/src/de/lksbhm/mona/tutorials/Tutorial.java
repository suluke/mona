package de.lksbhm.mona.tutorials;

import com.badlogic.gdx.utils.Disposable;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerAdapter;
import de.lksbhm.gdx.contexts.ContextListener;
import de.lksbhm.mona.levels.Level;

public abstract class Tutorial implements Disposable {
	private final Level level;
	private boolean isStarted = false;

	public Tutorial(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	private final ContextListener contextListener = new AbstractContextListenerAdapter<Level>(
			Level.class) {

		@Override
		protected void onEnterContext(Level level) {
			if (level != getLevel() || level.isSolved()) {
				return;
			}
			isStarted = true;
			start();
		}

		@Override
		protected void onLeaveContext(Level context) {
			if (isStarted) {
				end();
				isStarted = false;
			}
		}
	};

	public void load() {
		LksBhmGame.getGame().getContextManager().addListener(contextListener);
	}

	@Override
	public void dispose() {
		if (!LksBhmGame.getGame().getContextManager()
				.removeListener(contextListener)) {
			throw new RuntimeException("Leaking ContextListeners");
		}
	}
	
	public abstract String getCanonicalClassName();

	protected abstract void start();

	protected abstract void end();
}
