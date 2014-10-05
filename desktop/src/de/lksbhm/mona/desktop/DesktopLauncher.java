package de.lksbhm.mona.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.lksbhm.mona.DesktopPlatform;
import de.lksbhm.mona.Mona;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		LwjglApplicationConfiguration.disableAudio = true;
		config.preferencesDirectory = ".config/lksbhm/mona";
		config.foregroundFPS = 30;
		config.backgroundFPS = 20;
		config.title = "Mona";
		config.addIcon("textures/icon32.png", FileType.Internal);
		config.addIcon("textures/icon128.png", FileType.Internal);
		config.width = 360;
		config.height = 600;
		Mona mona = new Mona();
		new DesktopPlatform().register();
		new LwjglApplication(mona, config);
	}
}
