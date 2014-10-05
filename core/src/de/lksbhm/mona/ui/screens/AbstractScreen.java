package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;

public abstract class AbstractScreen extends
		de.lksbhm.gdx.ui.screens.AbstractScreen {
	public AbstractScreen() {
		super(getDefaultViewportWidth(), getDefaultViewportHeight());
		if (getClass() != SplashScreen.class
				&& getClass() != LoadingScreen.class
				&& getClass() != MainMenuScreen.class) {
			switch (Gdx.app.getType()) {
			// nothing to do
			case Android:
				break;
			case Desktop:
				break;
			case HeadlessDesktop:
				break;
			// needs software back button
			case Applet:
			case WebGL:
			case iOS: {
				Stage stage = getStage();
				Table base = getBaseTable();

				float originalHeight = base.getHeight();
				float newBaseHeight = originalHeight * .9f;

				base.setFillParent(false);
				base.setBounds(0, originalHeight - newBaseHeight,
						stage.getWidth(), newBaseHeight);

				Skin skin = LksBhmGame.getGame().getDefaultSkin();
				ImageButton backButton = new ImageButton(skin,
						"abstractScreen.backButton");
				backButton.setBounds(0, 0, stage.getWidth(), originalHeight
						- newBaseHeight);
				backButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						Gdx.input.getInputProcessor().keyUp(Keys.BACK);
					}
				});
				stage.addActor(backButton);
				break;
			}
			default:
				break;
			}
		}
	}

	private static int getDefaultViewportWidth() {
		return 360;
	}

	private static int getDefaultViewportHeight() {
		return 600;
	}

	public static abstract class AbstractBackButtonHandler extends InputAdapter {
		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
				onBackButtonPressed();
				return true;
			}
			return false;
		}

		protected abstract void onBackButtonPressed();
	}

	public static class BackButtonToMainMenuHandler extends
			AbstractBackButtonHandler {
		@Override
		protected void onBackButtonPressed() {
			LksBhmGame.getGame().getRouter()
					.changeScreen(MainMenuScreen.class, null);
		}
	};
}
