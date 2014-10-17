package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().fadeOutFadeIn().duration(.6f).get();
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	};
	private Label rewardCountLabel;
	private ImageTextButton resetButton;
	private Dialog resetPrompt;

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

		// reset button
		resetButton = new ImageTextButton("reset progress", skin,
				"userScreen.reset");
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resetPrompt.show(UserScreen.this.getStage());
			}
		});

		// yes/no dialog
		resetPrompt = new Dialog("", skin) {
			@Override
			protected void result(Object result) {
				if (!(result instanceof Boolean)) {
					throw new RuntimeException();
				}
				boolean res = (Boolean) result;
				if (res) {
					LksBhmGame.getGame().getUserManager().getCurrentUser()
							.reset();
					Transition transition = TransitionBuilder.newTransition()
							.slideInLeftExtraDistance().fadeClearColors()
							.duration(.6f).get();
					MainMenuScreen.showAsCurrentScreen(transition);
				}
			}
		};

		final float width = getStage().getWidth();

		Label questionLabel = new Label("Really want to reset all user data?",
				skin, "userScreen.resetQuestion");
		questionLabel.setWrap(true);
		resetPrompt.text(questionLabel);
		resetPrompt.getContentTable().getCell(questionLabel)
				.width(width * 0.9f);

		float buttonWidth = width * 0.4f;
		float buttonHeight = getBaseTable().getHeight() * 0.15f;
		// yes button
		ImageTextButton yesButton = new ImageTextButton("yes", skin,
				"userScreen.yes");
		resetPrompt.button(yesButton, Boolean.TRUE);
		resetPrompt.getButtonTable().getCell(yesButton)
				.size(buttonWidth, buttonHeight);

		// no button
		ImageTextButton noButton = new ImageTextButton("no", skin,
				"userScreen.no");
		resetPrompt.button(noButton, Boolean.FALSE);
		resetPrompt.getButtonTable().getCell(noButton)
				.size(buttonWidth, buttonHeight);
	}

	@Override
	protected void onShow() {
		super.onShow();
		layoutWidgets();
	}

	private void layoutWidgets() {
		getBaseTable().clear();
		float worldWidth = getBaseTable().getWidth();
		float worldHeight = getBaseTable().getHeight();
		float w = worldWidth * 0.9f;
		float h = worldHeight * 0.15f;

		rewardCountLabel.setText("Rewards: "
				+ Mona.getGame().getUserManager().getCurrentUser()
						.getRewardCount());
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
