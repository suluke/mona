package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

public class GameWonScreen extends AbstractScreen {

	private Label youWonLabel;
	private TextButton nextButton;
	private final InputAdapter backButtonHandler = new InputAdapter() {
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

	public GameWonScreen() {
		setClearColor(.422f, .816f, .147f, 1);
	}

	private void setupWidgets() {
		youWonLabel = new Label("puzzle  solved", LksBhmGame.getGame()
				.getDefaultSkin(), "won");
		youWonLabel.setAlignment(Align.center);
		nextButton = new TextButton("next", LksBhmGame.getGame()
				.getDefaultSkin(), "play");
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Router router = LksBhmGame.getGame().getRouter();
				Transition transition = TransitionBuilder.buildNew()
						.slideInRight().interpolateClearColor().get();
				transition.setDuration(.6f);
				router.changeScreen(MainMenuScreen.class, null, transition);
			}
		});
	}

	@Override
	public void onShow() {
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(Gdx.input.getInputProcessor());
		mux.addProcessor(backButtonHandler);
		Gdx.input.setInputProcessor(mux);
		getBaseTable().clear();
		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		Viewport vp = getViewport();

		float w, h;
		w = vp.getWorldWidth() * 0.6f;
		h = vp.getWorldHeight() * 0.15f;
		base.add(youWonLabel).size(w, h).center().row();

		base.add(nextButton).size(w, h).expandY().bottom().padBottom(40);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	public void setState(Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

}
