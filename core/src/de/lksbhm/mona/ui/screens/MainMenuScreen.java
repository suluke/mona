package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
import de.lksbhm.gdx.util.Loadable;
import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.levels.LevelPackageCollection;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.QualityPuzzleGenerator;

public class MainMenuScreen extends AbstractScreen {

	private TextButton playButton;
	private TextButton dailiesButton;
	private TextButton randomLevelButton;
	private ImageButton infoButton;
	private ImageButton userButton;
	private Label title;
	private final ShowDailyPackagesScreen showDailyPackagesScreen = new ShowDailyPackagesScreen();
	private Loadable<LevelPackageCollection> dailyPackagesLoader;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Gdx.app.exit();
		}
	};

	public MainMenuScreen() {
		setClearColor(0.518f, 0.863f, 0.796f, 1f);
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);
	}

	private void setupWidgets() {
		Skin skin = LksBhmGame.getGame().getDefaultSkin();
		title = new Label("MONA", skin, "title");

		playButton = new TextButton("play", skin, "play");
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Mona mona = LksBhmGame.getGame(Mona.class);
				Transition transition = TransitionBuilder.buildNew()
						.slideInRight().fadeClearColors().duration(.6f).get();
				LevelPackageManager pacman = mona.getLevelPackageManager();
				PackagesListScreen.showAsCurrentScreen(
						pacman.getInternalPackages(), transition);
			}
		});

		dailiesButton = new TextButton("dailies", skin, "play");
		dailiesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Transition transition = TransitionBuilder.buildNew()
						.slideInRight().fadeClearColors().duration(.6f).get();
				Mona mona = LksBhmGame.getGame(Mona.class);
				LevelPackageManager pacman = mona.getLevelPackageManager();
				dailyPackagesLoader = pacman.getDailyPackagesLoader();
				if (dailyPackagesLoader.getProgress() != 1) {
					LoadingScreen.showAsCurrentScreen(dailyPackagesLoader,
							showDailyPackagesScreen, getClearColor(), .6f,
							transition);
				} else {
					PackagesListScreen.showAsCurrentScreen(
							dailyPackagesLoader.get(), transition);
				}
			}
		});

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
										.generateRandomSeedAndPuzzle(new RandomXS128());
								ps.setPuzzle(generated.getSecond());
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.buildNew().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		infoButton = new ImageButton(skin, "about");
		infoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Router router = LksBhmGame.getGame().getRouter();
				Transition transition = TransitionBuilder.buildNew()
						.fadeClearColors().fadeOutFadeIn().duration(.6f).get();
				router.changeScreen(InfoScreen.class, null, transition);
			}
		});

		userButton = new ImageButton(skin, "user");
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		Viewport vp = getViewport();
		float worldWidth = vp.getWorldWidth();
		float worldHeight = vp.getWorldHeight();
		float w, h;
		w = worldWidth * 0.6f;
		h = worldHeight * 0.15f;
		title.setAlignment(Align.center);
		title.setFontScale(2);
		base.add(title).size(w, h).top().spaceBottom(50).colspan(3).row();
		base.add(playButton).size(w, h).center().spaceBottom(10).colspan(3)
				.row();
		base.add(dailiesButton).size(w, h).center().spaceBottom(10).colspan(3)
				.row();
		base.add(randomLevelButton).size(w, h).center().spaceBottom(10)
				.colspan(3).row();
		w = worldWidth * .2f;
		h = worldHeight * .05f;
		base.add(userButton).size(w, h);
		base.add(infoButton).size(w, h);
	}

	@Override
	public void onShow() {
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
		return false;
	}

	@Override
	public void setState(Object state) {
	}

	@Override
	public Object getState() {
		return null;
	}

	private class ShowDailyPackagesScreen implements Runnable {

		@Override
		public void run() {
			Transition transition = TransitionBuilder.buildNew()
					.fadeOutFadeIn().fadeClearColors().duration(.6f).get();
			PackagesListScreen.showAsCurrentScreen(dailyPackagesLoader.get(),
					transition);
		}

	}
}