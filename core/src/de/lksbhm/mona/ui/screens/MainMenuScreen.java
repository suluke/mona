package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.QualityPuzzleGenerator;

public class MainMenuScreen extends AbstractScreen {

	private TextButton playButton;
	private TextButton dailiesButton;
	private TextButton randomLevelButton;
	private Label title;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Gdx.app.exit();
		}
	};

	public MainMenuScreen() {
		setClearColor(0.518f, 0.863f, 0.796f, 1f);
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		title = new Label("MONA", skin, "title");

		playButton = new TextButton("play", skin, "play");
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Mona mona = (Mona) LksBhmGame.getGame();
				Transition transition = TransitionBuilder.buildNew()
						.slideInRight().interpolateClearColor().get();
				transition.setDuration(.6f);
				LevelPackageManager pacman = mona.getLevelPackageManager();
				PackagesListScreen.showAsCurrentScreen(
						pacman.getInternalPackages(), transition);
			}
		});

		dailiesButton = new TextButton("dailies", skin, "play");

		randomLevelButton = new TextButton("random", skin, "play");
		randomLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreen.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreen>() {
							@Override
							public void onObtained(RandomPuzzleScreen ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(new RandomXS128());
								ps.setPuzzle(generated.getSecond());
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.buildNew().slideInRight()
										.interpolateClearColor().get();
								transition.setDuration(.6f);
								router.changeScreen(ps, transition);
							}
						});
			}
		});
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		Viewport vp = getViewport();

		float w, h;
		w = vp.getWorldWidth() * 0.6f;
		h = vp.getWorldHeight() * 0.15f;
		title.setAlignment(Align.center);
		title.setFontScale(2);
		base.add(title).size(w, h).top().spaceBottom(50).row();
		base.add(playButton).size(w, h).center().spaceBottom(10).row();
		base.add(dailiesButton).size(w, h).center().spaceBottom(10).row();
		base.add(randomLevelButton).size(w, h).center().row();
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

	@Override
	public void onResourcesLoaded(AssetManager manager) {
		setupWidgets();
		// TODO hacky, find a better place
		ColorAction colorTransition = new ColorAction() {
			@Override
			protected void update(float percent) {
				super.update(percent);
				Color c = getColor();
				Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
			}
		};
		colorTransition.setColor(new Color(0, 0, 0, 1));
		colorTransition.setEndColor(getClearColor());
		colorTransition.setDuration(0.6f);
		this.getStage().addAction(colorTransition);
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return true;
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != MainMenuState.class) {
			throw new IllegalArgumentException(
					"Given state not suitable for MainMenuScreens");
		}
	}

	@Override
	public Object getState() {
		return new MainMenuState();
	}

	private static class MainMenuState {

	}
}