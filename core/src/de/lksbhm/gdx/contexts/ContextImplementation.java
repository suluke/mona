package de.lksbhm.gdx.contexts;

import de.lksbhm.gdx.LksBhmGame;

public class ContextImplementation implements Context {

	private final Context actualContext;

	public ContextImplementation(Context actualContex) {
		this.actualContext = actualContex;
	}

	protected ContextImplementation() {
		actualContext = this;
	}

	@Override
	public void enterContext() {
		ContextManager contextManager = LksBhmGame.getGame()
				.getContextManager();
		contextManager.enterContext(actualContext);
	}

	@Override
	public void leaveContext() {
		ContextManager contextManager = LksBhmGame.getGame()
				.getContextManager();
		contextManager.leaveContext(actualContext);
	}

}
