package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.gdx.util.Version;
import de.lksbhm.mona.Mona;

public class AboutScreen extends AbstractScreen {

	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().slideInLeft().duration(.6f).get();
			InfoScreen.setAsCurrentScreen(transition);
		}
	};

	private Image bannerImage;
	private Label versionLabel;

	public AboutScreen() {
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Mona game = LksBhmGame.getGame(Mona.class);
		Skin skin = game.getDefaultSkin();
		Version version = game.getVersion();

		bannerImage = new Image(skin, "banner.white");

		versionLabel = new Label("Version " + version.majorMinorRevision()
				+ "\n(" + version.getStatus().toString().toLowerCase() + ")",
				skin, "aboutScreen.version");

		// Only needed once
		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		base.align(Align.center);
		float width = getStage().getWidth();

		bannerImage.setScaling(Scaling.fit);
		base.add(bannerImage).row();

		versionLabel.setAlignment(Align.center);
		base.add(versionLabel).width(width * .9f);
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

	public static void setAsCurrentScreen(final Transition transition) {
		final Router router = LksBhmGame.getGame().getRouter();
		router.obtainScreen(AboutScreen.class,
				new ResourceConsumerObtainedCallback<AboutScreen>() {
					@Override
					public void onObtained(AboutScreen screen) {
						router.changeScreen(screen, transition);
					}
				});
	}
}
