package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import de.lksbhm.gdx.LksBhmGame;

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

	protected static class BackButtonToMainMenuHandler extends InputAdapter {
		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Keys.BACK) {
				LksBhmGame.getGame().getRouter()
						.changeScreen(MainMenuScreen.class, null);
				return true;
			}
			return false;
		}
	};
}
