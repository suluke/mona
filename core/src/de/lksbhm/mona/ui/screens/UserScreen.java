package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.Mona;

public class UserScreen extends AbstractScreen {

	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Router router = LksBhmGame.getGame().getRouter();
			Transition transition = TransitionBuilder.buildNew()
					.fadeClearColors().fadeOutFadeIn().duration(.6f).get();
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	};
	private Label rewardCountLabel;
	private ImageTextButton resetButton;

	public UserScreen() {
		setClearColor(.816f, .588f, .29f, 1);
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		rewardCountLabel = new Label("", skin, "userScreen.rewardCount");
		resetButton = new ImageTextButton("reset progress", skin,
				"userScreen.reset");
	}

	@Override
	protected void onShow() {
		super.onShow();
		layoutWidgets();
	}

	private void layoutWidgets() {
		getBaseTable().clear();
		float worldWidth = getStage().getWidth();
		float worldHeight = getStage().getHeight();
		float w = worldWidth * 0.9f;
		float h = worldHeight * 0.15f;

		rewardCountLabel.setText("Rewards: "
				+ LksBhmGame.getGame(Mona.class).getUserManager()
						.getCurrentUser().getRewardCount());
		getBaseTable().add(rewardCountLabel).spaceBottom(h).row();

		resetButton.getLabel().setFontScale(.75f);
		getBaseTable().add(resetButton).size(w, h);
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
