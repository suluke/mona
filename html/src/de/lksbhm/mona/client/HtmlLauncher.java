package de.lksbhm.mona.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.lksbhm.mona.HTMLPlatform;
import de.lksbhm.mona.Mona;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 320);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		new HTMLPlatform().register();
		return new Mona();
	}
}