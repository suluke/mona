package de.lksbhm.mona.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.lksbhm.mona.AndroidPlatform;
import de.lksbhm.mona.Mona;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		// Check if there is already a game
		Mona mona = Mona.getGame();
		if (mona == null) {
			mona = new Mona();
			new AndroidPlatform().register();
			initialize(mona, config);
		} else {
			// TODO take advantage of having most of the code still in ram

			// Class<? extends TransitionableResettableConsumerScreen>
			// previousScreen = mona
			// .getScreen().getClass();

			// TODO a state would be good. But history with size 1 seems a
			// little overpowered, though
			// mona.getRouter().changeScreen(previousScreen, null);

			initialize(mona, config);

			// force reloading of screens and assets because of lost textures
			// Calles after initialize() because of new Gdx.graphics,
			// Gdx.files...
			mona.reloadResources();
		}
	}
}
