package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerHelper;

class DropInBehavior {
	public void load() {
		LksBhmGame
				.getGame()
				.getContextManager()
				.addListener(
						new AbstractContextListenerHelper<Mona>(Mona.class) {
							@Override
							protected void onEnterContext(Mona context) {
								System.out.println("You play Mona!");
							}

							@Override
							protected void onLeaveContext(Mona context) {
							}
						});
	}
}
