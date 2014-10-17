package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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

	private final Table contentTable = new Table();
	private final ScrollPane scrollPane = new ScrollPane(contentTable);

	private Image bannerImage;
	private Label versionLabel;
	private Label createdByLabel;
	private Label createdByNameLabel;
	private Label artworkByLabel;
	private Label artworkByNameLabel;
	private Label musicByLabel;
	private Label musicByNameLabel;

	public AboutScreen() {
		InputMultiplexer mux = new InputMultiplexer(getStage(),
				backButtonHandler);
		setInputProcessor(mux);

		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setOverscroll(false, false);
	}

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
	}

	private void setupWidgets() {
		Mona game = Mona.getGame();
		Skin skin = game.getDefaultSkin();
		Version version = game.getVersion();

		bannerImage = new Image(skin, "banner.white");

		versionLabel = new Label("Version " + version.majorMinorRevision()
				+ "\n(" + version.getStatus().toString().toLowerCase() + ")",
				skin, "aboutScreen.version");

		createdByLabel = new Label("Created by:", skin);
		createdByNameLabel = new Label("Lukas Böhm", skin);

		artworkByLabel = new Label("Artwork by:", skin);
		artworkByNameLabel = new Label("???", skin);

		musicByLabel = new Label("Music by:", skin);
		musicByNameLabel = new Label("???", skin);

		// Only needed once
		layoutWidgets();
	}

	private void layoutWidgets() {
		final float width = getBaseTable().getWidth();
		final float height = getBaseTable().getHeight();

		getBaseTable().add(scrollPane).fill();
		contentTable.align(Align.center);
		contentTable.padLeft(width * .05f);
		contentTable.padRight(width * .05f);

		bannerImage.setScaling(Scaling.fit);
		contentTable.add(bannerImage).height(height * .2f).colspan(2).row();

		versionLabel.setAlignment(Align.center);
		versionLabel.setFontScale(.8f);
		contentTable.add(versionLabel).width(width * .9f).colspan(2)
				.spaceBottom(height * 0.2f).row();

		createdByLabel.setFontScale(.6f);
		contentTable.add(createdByLabel);

		createdByNameLabel.setFontScale(.6f);
		contentTable.add(createdByNameLabel).expandX().row();

		artworkByLabel.setFontScale(.6f);
		contentTable.add(artworkByLabel);

		artworkByNameLabel.setFontScale(.6f);
		contentTable.add(artworkByNameLabel).expandX().row();

		musicByLabel.setFontScale(.6f);
		contentTable.add(musicByLabel);

		musicByNameLabel.setFontScale(.6f);
		contentTable.add(musicByNameLabel).expandX().row();
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
