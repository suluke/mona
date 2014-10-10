package de.lksbhm.mona.mock;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;

public class Mock {
	public static void simpleMock() {
		if (LksBhmGame.getGame() == null) {
			new Mona() {
				// TODO maybe overwrite some stuff?
			};
		}
	}
}
