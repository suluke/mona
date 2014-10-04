package de.lksbhm.mona.ui.screens;

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
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

public class PuzzleSolvedScreen extends AbstractScreen {

	private Label youWonLabel;
	private TextButton nextButton;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.slideInLeftExtraDistance().fadeClearColors().duration(.6f)
					.get();
			LksBhmGame.getGame().getRouter()
					.changeScreen(MainMenuScreen.class, null, transition);
		}
	};

	public PuzzleSolvedScreen() {
		setClearColor(.422f, .816f, .147f, 1);
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);
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
				Transition transition = TransitionBuilder.newTransition()
						.slideInRight().fadeClearColors().duration(.6f).get();
				RandomSelectionScreen.showAsCurrentScreen(transition);
			}
		});
		layoutWidgets();
	}

	private void layoutWidgets() {
		getBaseTable().clear();
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
	}

	@Override
	public Object getState() {
		return null;
	}

}
