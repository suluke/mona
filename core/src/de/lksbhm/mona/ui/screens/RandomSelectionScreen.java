package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.levels.Difficulty;
import de.lksbhm.mona.puzzle.Generator;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.QualityPuzzleGenerator;

public class RandomSelectionScreen extends AbstractScreen {

	private TextButton veryEasyButton;
	private TextButton easyButton;
	private TextButton mediumButton;
	private TextButton hardButton;
	private TextButton veryHardButton;

	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.fadeClearColors().slideInLeft().duration(.6f).get();
			MainMenuScreen.showAsCurrentScreen(transition);
		}
	};

	public RandomSelectionScreen() {
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

		veryEasyButton = new TextButton("very easy", skin,
				"randomSelectionScreen.button");
		veryEasyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreenOne.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreenOne>() {
							@Override
							public void onObtained(RandomPuzzleScreenOne ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(
												Generator.random,
												new RandomXS128(),
												Difficulty.VERY_EASY);
								ps.setup(generated.getSecond(),
										Difficulty.VERY_EASY);
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.newTransition().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		easyButton = new TextButton("easy", skin,
				"randomSelectionScreen.button");
		easyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreenOne.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreenOne>() {
							@Override
							public void onObtained(RandomPuzzleScreenOne ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(
												Generator.random,
												new RandomXS128(),
												Difficulty.EASY);
								ps.setup(generated.getSecond(), Difficulty.EASY);
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.newTransition().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		mediumButton = new TextButton("medium", skin,
				"randomSelectionScreen.button");
		mediumButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreenOne.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreenOne>() {
							@Override
							public void onObtained(RandomPuzzleScreenOne ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(
												Generator.random,
												new RandomXS128(),
												Difficulty.MEDIUM);
								ps.setup(generated.getSecond(),
										Difficulty.MEDIUM);
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.newTransition().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		hardButton = new TextButton("hard", skin,
				"randomSelectionScreen.button");
		hardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreenOne.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreenOne>() {
							@Override
							public void onObtained(RandomPuzzleScreenOne ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(
												Generator.random,
												new RandomXS128(),
												Difficulty.HARD);
								ps.setup(generated.getSecond(), Difficulty.HARD);
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.newTransition().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		veryHardButton = new TextButton("very hard", skin,
				"randomSelectionScreen.button");
		veryHardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Router router = LksBhmGame.getGame().getRouter();
				router.obtainScreen(
						RandomPuzzleScreenOne.class,
						new ResourceConsumerObtainedCallback<RandomPuzzleScreenOne>() {
							@Override
							public void onObtained(RandomPuzzleScreenOne ps) {
								Pair<Long, Puzzle> generated = QualityPuzzleGenerator
										.generateSeedAndPuzzle(
												Generator.random,
												new RandomXS128(),
												Difficulty.VERY_HARD);
								ps.setup(generated.getSecond(),
										Difficulty.VERY_HARD);
								ps.setSeed(generated.getFirst());
								Transition transition = TransitionBuilder
										.newTransition().slideInRight()
										.fadeClearColors().duration(.6f).get();
								router.changeScreen(ps, transition);
							}
						});
			}
		});

		layoutWidgets();
	}

	private void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();

		final float width = getBaseTable().getWidth();
		final float height = getBaseTable().getHeight();

		final float buttonWidth = width * .9f;
		final float buttonHeight = height * .15f;
		final float buttonSpace = buttonHeight * .2f;

		base.add(veryEasyButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpace).row();
		base.add(easyButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpace).row();
		base.add(mediumButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpace).row();
		base.add(hardButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpace).row();
		base.add(veryHardButton).size(buttonWidth, buttonHeight).row();

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

	public static void showAsCurrentScreen(final Transition transition) {
		final Router router = LksBhmGame.getGame().getRouter();
		router.changeScreen(RandomSelectionScreen.class, null, transition);
	}
}
