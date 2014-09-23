package de.lksbhm.mona.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.lksbhm.mona.HtmlPlatform;
import de.lksbhm.mona.Mona;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 800);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		Mona mona = new Mona();
		new HtmlPlatform().register();
		return mona;
	}
}