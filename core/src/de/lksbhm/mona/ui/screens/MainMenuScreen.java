package de.lksbhm.mona.ui.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.ui.screens.transitions.InterpolateClearColor;
import de.lksbhm.gdx.ui.screens.transitions.SlideInRight;
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
				Router router = LksBhmGame.getGame().getRouter();
				PuzzleScreen ps = router.obtainScreen(PuzzleScreen.class);
				Puzzle puzzle = Generator.generate(5, 10,
						new RandomXS128(1, 4), 1.0f, 1.0f);
				ps.setPuzzle(puzzle);
				// TODO implement pooling
				SlideInRight slide = new SlideInRight();
				InterpolateClearColor blendColors = new InterpolateClearColor();
				slide.runParallel(blendColors);
				slide.setDuration(.6f);
				router.changeScreen(ps, slide);
			}
		});

		dailiesButton = new TextButton("dailies", LksBhmGame.getGame()
				.getDefaultSkin(), "play");

		randomLevelButton = new TextButton("random", LksBhmGame.getGame()
				.getDefaultSkin(), "play");
		randomLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Router router = LksBhmGame.getGame().getRouter();
				PuzzleScreen ps = router.obtainScreen(PuzzleScreen.class);
				Puzzle puzzle = Generator.generate(new Random(), 1.f, 1.f);
				ps.setPuzzle(puzzle);
				// TODO implement pooling
				SlideInRight slide = new SlideInRight();
				InterpolateClearColor blendColors = new InterpolateClearColor();
				slide.runParallel(blendColors);
				slide.setDuration(.6f);
				router.changeScreen(ps, slide);
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