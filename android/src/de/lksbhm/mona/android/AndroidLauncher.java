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
		Mona mona = new Mona();
		new AndroidPlatform().register();
		initialize(mona, config);
	}
}
