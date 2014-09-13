package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerHelper;

/**
 * Must not access anything that gets instantiated/loaded/set after
 * game.initialize
 *
 */
class DropInBehavior {
	public void load() {
		loadTutorialsOnFirstStartBehavior();
	}

	private void loadTutorialsOnFirstStartBehavior() {
		LksBhmGame
				.getGame(Mona.class)
				.getContextManager()
				.addListener(
						new AbstractContextListenerHelper<Mona>(Mona.class) {
							@Override
							protected void onEnterContext(Mona mona) {
								if (!mona.getUserManager().getCurrentUser()
										.hasPlayedTutorials()) {
									System.out.println("Play tutorial!");
								}
							}

							@Override
							protected void onLeaveContext(Mona mona) {
							}
						});
	}
}
