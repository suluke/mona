package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;

public class InfoScreen extends AbstractScreen {
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Router router = LksBhmGame.getGame().getRouter();
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().fadeOutFadeIn().duration(.6f).get();
			router.changeScreen(MainMenuScreen.class, null, transition);
		}
	};

	private ImageTextButton aboutTheGameButton;
	private ImageTextButton howToPlayButton;
	private ImageTextButton licensesButton;

	public InfoScreen() {
		setClearColor(.816f, .588f, .29f, 1);
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);
	}

	@Override
	protected void onShow() {
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		Table base = getBaseTable();

		float w = getStage().getWidth() * .9f;
		float h = getStage().getHeight() * 0.1f;
		float bottomSpace = h * .8f;
		float padTop = h * .15f;
		float fontScale = .7f;
		float imgSize = h * .7f;
		float labelHeight = h * .7f;

		// create and add outer widgets first so they get a size
		howToPlayButton = new ImageTextButton("How to play", skin, "howToPlay");
		howToPlayButton.align(Align.left);
		howToPlayButton.getLabel().setFontScale(fontScale);
		howToPlayButton.getLabelCell().height(labelHeight);
		howToPlayButton.getImageCell().size(imgSize).left().padTop(padTop);
		howToPlayButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Transition transition = TransitionBuilder.newTransition()
						.slideInRight().fadeClearColors().duration(.6f)
						.disposeAllOnFinish(false).get();
				HowToPlayScreen.setAsCurrentScreen(transition);
			}
		});

		aboutTheGameButton = new ImageTextButton("About the game", skin,
				"aboutTheGame");
		aboutTheGameButton.align(Align.left);
		aboutTheGameButton.getLabel().setFontScale(fontScale);
		aboutTheGameButton.getLabelCell().height(labelHeight);
		aboutTheGameButton.getImageCell().size(imgSize).left().padTop(padTop);
		aboutTheGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Transition transition = TransitionBuilder.newTransition()
						.slideInRight().fadeClearColors().duration(.6f)
						.disposeAllOnFinish(false).get();
				AboutScreen.setAsCurrentScreen(transition);
			}
		});

		licensesButton = new ImageTextButton("Licenses", skin, "licenses");
		licensesButton.align(Align.left);
		licensesButton.getLabel().setFontScale(fontScale);
		licensesButton.getLabelCell().height(labelHeight);
		licensesButton.getImageCell().size(imgSize).left().padTop(padTop);
		licensesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Transition transition = TransitionBuilder.newTransition()
						.slideInRight().fadeClearColors().duration(.6f)
						.disposeAllOnFinish(false).get();
				LicensesScreen.setAsCurrentScreen(transition);
			}
		});

		base.add(howToPlayButton).size(w, h).spaceBottom(bottomSpace).row();
		base.add(aboutTheGameButton).size(w, h).spaceBottom(bottomSpace).row();
		base.add(licensesButton).size(w, h);
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		// TODO Auto-generated method stub
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

	public static void setAsCurrentScreen(final Transition transition) {
		final Router router = LksBhmGame.getGame().getRouter();
		router.obtainScreen(InfoScreen.class,
				new ResourceConsumerObtainedCallback<InfoScreen>() {
					@Override
					public void onObtained(InfoScreen screen) {
						router.changeScreen(screen, transition);
					}
				});
	}

}
