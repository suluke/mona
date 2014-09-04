package de.lksbhm.mona.tutorials.p00;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerHelper;
import de.lksbhm.gdx.contexts.ContextListener;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.tutorials.Tutorial;

public class Tutorial00 extends Tutorial {

	private final ContextListener contextListener = new AbstractContextListenerHelper<LiterallyDefinedLevel>(
			LiterallyDefinedLevel.class) {
		@Override
		protected void onEnterContext(LiterallyDefinedLevel context) {
			if (context != getLevel()) {
				return;
			}

			System.out.println("You are playing level 00");
		}

		@Override
		protected void onLeaveContext(LiterallyDefinedLevel context) {
			// TODO Auto-generated method stub

		}
	};

	public Tutorial00(Level level) {
		super(level);
	}

	@Override
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

}
