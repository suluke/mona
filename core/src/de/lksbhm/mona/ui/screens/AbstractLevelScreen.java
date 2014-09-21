package de.lksbhm.mona.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelPackage;

abstract class AbstractLevelScreen extends AbstractPuzzleScreen {
	private Level l;
	private Label idLabel;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.buildNew().slideInLeft()
					.fadeClearColors().duration(.6f).get();
			PackageScreen.showAsCurrentScreen(l.getPackage(), transition);
		}
	};

	public AbstractLevelScreen() {
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);
	}

	public void setLevel(Level l) {
		this.l = l;
		l.reset();
		idLabel.setText(l.getPackage().getDisplayName() + "/" + l.getLevelId());
		setPuzzle(l.getPuzzle());
	}

	@Override
	protected void onShow() {
		super.onShow();
		l.enterContext();
	}

	@Override
	protected void setupWidgets() {
		super.setupWidgets();
		idLabel = new Label("", LksBhmGame.getGame().getDefaultSkin());
	}

	@Override
	protected void layoutWidgets() {
		Table base = getBaseTable();
		base.clear();
		base.add(idLabel).row();
		base.add(getPuzzleActor()).height(0.9f * getDefaultViewportHeight());
	}

	@Override
	protected void onHide() {
		super.onHide();
		l.leaveContext();
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	protected void onWin() {
		super.onWin();
		Level next = l.getNextLevel();
		final Router router = LksBhmGame.getGame().getRouter();
		if (next != null) {
			goToNextLevel(next, router);
		} else {
			router.obtainScreen(PackageScreen.class,
					new ResourceConsumerObtainedCallback<PackageScreen>() {
						@Override
						public void onObtained(final PackageScreen packageScreen) {
							router.obtainScreen(
									RewardScreen.class,
									new ResourceConsumerObtainedCallback<RewardScreen>() {
										@Override
										public void onObtained(
												RewardScreen rewardScreen) {
											LevelPackage pack = l.getPackage();
											packageScreen.setLevelPackage(pack);
											rewardScreen.setup(
													pack.getReward(),
													packageScreen);
											Transition transition = TransitionBuilder
													.buildNew().slideInRight()
													.fadeClearColors()
													.duration(.6f).get();
											router.changeScreen(rewardScreen,
													transition);
										}
									});
						}
					});
		}
	}

	protected abstract void goToNextLevel(final Level next, final Router router);

	protected void switchToNextLevelScreen(AbstractLevelScreen screen,
			Level next, Router router) {
		screen.setLevel(next);
		Transition transition = TransitionBuilder.buildNew().slideInRight()
				.fadeClearColors().duration(.6f).get();
		router.changeScreen(screen, transition);
	}
}
