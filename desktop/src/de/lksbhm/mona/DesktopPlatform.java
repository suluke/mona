package de.lksbhm.mona;

import de.lksbhm.mona.Platform.PlatformInterface;

public class DesktopPlatform implements PlatformInterface {
	public void register() {
		Platform.setPlatform(this);
	}
}
