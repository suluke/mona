package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.ui.actors.PagedScrollPane;

public class LicensesScreen extends AbstractScreen {

	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().slideInLeft().duration(.6f).get();
			InfoScreen.setAsCurrentScreen(transition);
		}
	};

	private final PagedScrollPane scrollPane = new PagedScrollPane();
	private final Table libgdxPage = new Table();
	private Image libgdxBanner;
	private Label libgdxVersion;
	private Label libgdxLicense;

	public LicensesScreen() {
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
		setupLibGDXPage(skin);
		layoutWidgets();
	}

	private void setupLibGDXPage(Skin skin) {
		libgdxPage.setBackground(skin.getDrawable("white"));

		libgdxBanner = new Image(skin, "licensesScreen.banner.libgdx");
		libgdxVersion = new Label(Version.VERSION, skin,
				"licensesScreen.version");
		// TODO
		libgdxLicense = new Label("<Enter license here>", skin,
				"licensesScreen.license");
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		float width = base.getWidth();
		float height = base.getHeight();
		scrollPane.setSize(width, height);

		layoutLibGDXPage();
		scrollPane.addPage(libgdxPage);
		base.add(scrollPane).size(base.getWidth(), base.getHeight()).fill();
	}

	private void layoutLibGDXPage() {
		Table base = getBaseTable();
		float width = base.getWidth();
		float height = base.getHeight();
		libgdxPage.setSize(width, height);

		libgdxPage.add(libgdxBanner).padTop(height * 0.1f).row();

		libgdxVersion.setFontScale(.7f);
		libgdxVersion.setAlignment(Align.center);
		libgdxPage.add(libgdxVersion).row();

		libgdxLicense.setWrap(true);
		libgdxLicense.setFontScale(.4f);
		libgdxPage.add(libgdxLicense).width(width * .9f).expandY().row();
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
		router.obtainScreen(LicensesScreen.class,
				new ResourceConsumerObtainedCallback<LicensesScreen>() {
					@Override
					public void onObtained(LicensesScreen screen) {
						router.changeScreen(screen, transition);
					}
				});
	}
}
