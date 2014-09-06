package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.levels.LevelPackageManager;
import de.lksbhm.mona.puzzle.Generator;
import de.lksbhm.mona.puzzle.Puzzle;

public class MainMenuScreen extends AbstractScreen {

	private TextButton playButton;
	private TextButton dailiesButton;
	private TextButton randomLevelButton;
	private final InputAdapter backButtonHandler = new InputAdapter() {
		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Keys.BACK) {
				Gdx.app.exit();
				return true;
			}
			return false;
		}
	};

	public MainMenuScreen() {
		setClearColor(0.518f, 0.863f, 0.796f, 1f);
	}

	private void setupWidgets() {
		playButton = new TextButton("play", LksBhmGame.getGame()
				.getDefaultSkin(), "play");
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Mona mona = (Mona) LksBhmGame.getGame();
				final Router router = mona.getRouter();
				router.obtainScreen(
						PackagesListScreen.class,
						new ResourceConsumerObtainedCallback<PackagesListScreen>() {
							@Override
							public void onObtained(PackagesListScreen listScreen) {
								Transition transition = TransitionBuilder
										.buildNew().slideInRight()
										.interpolateClearColor().get();
								transition.setDuration(.6f);
								LevelPackageManager pacman = mona
										.getLevelPackageManager();
								listScreen.setLevelPackageCollection(pacman
										.getInternalPackages());
								router.changeScreen(listScreen, transition);
							}
						});
			}
		});

		dailiesButton = new TextButton("dailies", LksBhmGame.getGame()
				.getDefaultSkin(), "play");

		randomLevelButton = new TextButton("random", LksBhmGame.getGame()
				.getDefaultSkin(), "play");
		randomLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(PuzzleScreen.class,
						new ResourceConsumerObtainedCallback<PuzzleScreen>() {
							@Override
							public void onObtained(PuzzleScreen ps) {
								Puzzle puzzle = Generator.generate(1.f, 1.f);
								ps.setPuzzle(puzzle);
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