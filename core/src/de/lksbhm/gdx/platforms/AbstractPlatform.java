package de.lksbhm.gdx.platforms;

import de.lksbhm.gdx.LksBhmGame;

public class AbstractPlatform {
	public void register() {
		LksBhmGame.getGame().getPlatformManager().setPlatform(this);
	}
}
