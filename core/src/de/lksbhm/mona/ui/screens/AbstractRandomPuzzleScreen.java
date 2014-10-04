package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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

public abstract class AbstractRandomPuzzleScreen<NextScreenType extends AbstractRandomPuzzleScreen<?>>
		extends AbstractPuzzleScreen {

	private final AbstractRandomPuzzleScreenState state = new AbstractRandomPuzzleScreenState();
	private Label seedLabel;
	private long seed;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.newTransition()
					.slideInLeft().fadeClearColors().duration(.6f).get();
			RandomSelectionScreen.showAsCurrentScreen(transition);
		}

	};
	private boolean disposePuzzle = false;

	public AbstractRandomPuzzleScreen() {
		setClearColor(0.1f, 0.1f, 0.1f, 1f);
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);

		// make sure parent state instanceof AbstractRandomPuzzleScreenState and
		// changes are transparent for parent and this class
		setState(state);
	}

	@Override
	protected void onShow() {
		super.onShow();
	}

	@Override
	protected void onWin() {
		super.onWin();
		disposePuzzle = true;
		final Router router = LksBhmGame.getGame().getRouter();
		router.obtainScreen(getNextScreenTypeClass(),
				new ResourceConsumerObtainedCallback<NextScreenType>() {
					@Override
					public void onObtained(NextScreenType screen) {
						Transition transition = TransitionBuilder
								.newTransition().slideInRight()
								.fadeClearColors().duration(.6f).get();
						Pair<Long, Puzzle> generated = QualityPuzzleGenerator
								.generateSeedAndPuzzle(Generator.random,
										new RandomXS128(), state.difficulty);
						screen.setup(generated.getValue(), state.difficulty);
						screen.setSeed(generated.getKey());
						router.changeScreen(screen, transition);
					}
				});
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();
		seedLabel = new Label("" + seed, LksBhmGame.getGame().getDefaultSkin());
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	public void setState(Object state) {
		if (state.getClass() != AbstractRandomPuzzleScreenState.class) {
			throw new RuntimeException();
		}
		super.setState(state); // just for hooks in super type
		this.state.set((AbstractRandomPuzzleScreenState) state);
	}

	@Override
	public Object getState() {
		AbstractRandomPuzzleScreenState newState = new AbstractRandomPuzzleScreenState();
		newState.set(state);
		return newState;
	}

	@Override
	protected void layoutWidgets() {
		seedLabel.setFontScale(0.3f);

		Table base = getBaseTable();
		base.clear();
		base.add(getPuzzleActor()).height(getDefaultViewportHeight() * 0.9f)
				.row();
		base.add(seedLabel);
	}

	public void setup(Puzzle p, Difficulty difficulty) {
		setPuzzle(p);
		state.difficulty = difficulty;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		seedLabel.setText("" + seed);
	}

	@Override
	protected void onHide() {
		super.onHide();
		if (disposePuzzle) {
			state.p.dispose();
			state.p = null;
			super.setState(state);
			disposePuzzle = false;
		}
	}

	protected abstract Class<NextScreenType> getNextScreenTypeClass();

	private static class AbstractRandomPuzzleScreenState extends
			AbstractPuzzleScreenState {
		public Difficulty difficulty;

		public void set(AbstractRandomPuzzleScreenState other) {
			super.set(other);
			difficulty = other.difficulty;
		}
	}
}
