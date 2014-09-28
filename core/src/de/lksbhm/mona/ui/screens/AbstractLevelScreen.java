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
import de.lksbhm.mona.ui.actors.PuzzleActor;

public abstract class AbstractLevelScreen<NextLevelScreenType extends AbstractLevelScreen<?>>
		extends AbstractPuzzleScreen {
	private Level l;
	private Label idLabel;
	private boolean packageAlreadySolved = false;
	private final AbstractBackButtonHandler backButtonHandler = new AbstractBackButtonHandler() {
		@Override
		protected void onBackButtonPressed() {
			Transition transition = TransitionBuilder.buildNew().slideInLeft()
					.fadeClearColors().duration(.6f).get();
			PackageScreen.showAsCurrentScreen(l.getPackage(), transition);
		}
	};

	AbstractLevelScreen() {
		InputMultiplexer mux = new InputMultiplexer();
		mux.addProcessor(getStage());
		mux.addProcessor(backButtonHandler);
		setInputProcessor(mux);
	}

	public void setLevel(Level l) {
		this.l = l;
		if (l != null) {
			packageAlreadySolved = l.getPackage().isSolved();
			l.reset();
			idLabel.setText(l.getPackage().getDisplayName() + "/"
					+ l.getLevelId());
			setPuzzle(l.getPuzzle());
		} else {
			setPuzzle(null);
		}
	}

	@Override
	protected void onShow() {
		l.setView(this);
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
		l.setView(null);
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
		if (!packageAlreadySolved && l.getPackage().isSolved()) {
			switchToRewardScreen(next, router);
			packageAlreadySolved = true;
		} else {
			if (next != null) {
				switchToNextLevelScreen(next, router);
			} else {
				switchToPackageScreen(router);
			}
		}
	}

	protected abstract Class<NextLevelScreenType> getNextLevelScreenTypeClass();

	private void switchToRewardScreen(final Level next, final Router router) {
		final int reward = l.getPackage().getReward();
		final Transition transition = TransitionBuilder.buildNew()
				.fadeClearColors().slideInRight().duration(.6f).get();
		// TODO which is the better behavior? Currently solved like in lyne
		// if (next != null) {
		// router.obtainScreen(RewardScreen.class,
		// new ResourceConsumerObtainedCallback<RewardScreen>() {
		// @Override
		// public void onObtained(RewardScreen rewardScreen) {
		// rewardScreen.setup(reward, new Runnable() {
		// @Override
		// public void run() {
		// switchToNextLevelScreen(next, router);
		// }
		// });
		// router.changeScreen(rewardScreen, transition);
		// }
		// });
		// } else {
		router.obtainScreen(RewardScreen.class,
				new ResourceConsumerObtainedCallback<RewardScreen>() {
					@Override
					public void onObtained(RewardScreen rewardScreen) {
						rewardScreen.setup(reward, new Runnable() {
							@Override
							public void run() {
								switchToPackageScreen(router);
							}
						});
						router.changeScreen(rewardScreen, transition);
					}
				});
		// }
	}

	private void switchToNextLevelScreen(final Level next, final Router router) {
		router.obtainScreen(getNextLevelScreenTypeClass(),
				new ResourceConsumerObtainedCallback<NextLevelScreenType>() {
					@Override
					public void onObtained(NextLevelScreenType screen) {
						screen.setLevel(next);
						Transition transition = TransitionBuilder.buildNew()
								.slideInRight().fadeClearColors().duration(.6f)
								.get();
						router.changeScreen(screen, transition);
					}
				});
	}

	private void switchToPackageScreen(final Router router) {
		router.obtainScreen(PackageScreen.class,
				new ResourceConsumerObtainedCallback<PackageScreen>() {
					@Override
					public void onObtained(PackageScreen packageScreen) {
						LevelPackage pack = l.getPackage();
						packageScreen.setLevelPackage(pack);
						Transition transition = TransitionBuilder.buildNew()
								.slideInLeftExtraDistance().fadeClearColors()
								.duration(.6f).get();
						router.changeScreen(packageScreen, transition);
					}
				});
	}

	@Override
	public PuzzleActor getPuzzleActor() {
		return super.getPuzzleActor();
	}
}
