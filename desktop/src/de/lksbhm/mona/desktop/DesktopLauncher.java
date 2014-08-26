package de.lksbhm.mona.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.lksbhm.mona.DesktopPlatform;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.ui.screens.AbstractScreen;

public class DesktopLauncher {
	public static void main(String[] arg) {
		(new DesktopPlatform()).register();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		LwjglApplicationConfiguration.disableAudio = true;
		config.foregroundFPS = 30;
		config.backgroundFPS = 10;
		config.title = "Mona";
		config.addIcon("textures/icon32.png", FileType.Internal);
		config.addIcon("textures/icon128.png", FileType.Internal);
		config.width = AbstractScreen.getDefaultViewportWidth();
		config.height = AbstractScreen.getDefaultViewportHeight();
		new LwjglApplication(new Mona(), config);
	}
}
