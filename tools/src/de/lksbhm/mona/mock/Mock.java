package de.lksbhm.mona.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;

import de.lksbhm.mona.Mona;

public class Mock {
	private static Mona mona;
	private static Application gdx;

	public static void simpleInstantiationMock() {
		if (mona == null) {
			mona = new Mona() {
				// TODO maybe overwrite some stuff?
			};
		}
	}

	public static void headlessBackendMock() {
		GdxNativesLoader.disableNativesLoading = true;
		simpleInstantiationMock();
		if (gdx == null) {
			HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
			config.renderInterval = -1;
			gdx = new HeadlessApplication(mona, config);
		}
	}
}
