package de.lksbhm.mona.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.lksbhm.mona.DesktopPlatform;
import de.lksbhm.mona.Mona;

public class DesktopLauncher {
	public static void main(String[] arg) {
		(new DesktopPlatform()).register();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		LwjglApplicationConfiguration.disableAudio = true;
		new LwjglApplication(new Mona(), config);
	}
}
