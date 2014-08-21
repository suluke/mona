package de.lksbhm.mona.ui.screens;

public abstract class AbstractScreen extends
		de.lksbhm.gdx.ui.screens.AbstractScreen {
	public AbstractScreen() {
		super(getDefaultViewportWidth(), getDefaultViewportHeight());
	}

	public static int getDefaultViewportWidth() {
		return 360;
	}

	public static int getDefaultViewportHeight() {
		return 600;
	}
}
