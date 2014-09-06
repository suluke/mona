package de.lksbhm.mona.ui.screens;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.gdx.ui.screens.transitions.Transition;
import de.lksbhm.gdx.ui.screens.transitions.TransitionBuilder;
import de.lksbhm.mona.levels.Level;

public class LevelScreenOne extends AbstractPuzzleScreen {
	private Level l;

	public void setLevel(Level l) {
		this.l = l;
		l.reset();
		setPuzzle(l.getPuzzle());
	}

	@Override
	protected void onShow() {
		super.onShow();
		l.enterContext();
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
		Level next = l.getNextLevel();
		final Router router = LksBhmGame.getGame().getRouter();
		if (next != null) {
			goToNextLevel(next, router);
		} else {
			router.obtainScreen(PackageScreen.class,
					new ResourceConsumerObtainedCallback<PackageScreen>() {
						@Override
						public void onObtained(PackageScreen packageScreen) {
							packageScreen.setLevelPackage(l.getPackage());
							router.changeScreen(packageScreen);
						}
					});
		}
	}

	protected void goToNextLevel(final Level next, final Router router) {
		router.obtainScreen(LevelScreenTwo.class,
				new ResourceConsumerObtainedCallback<LevelScreenTwo>() {
					@Override
					public void onObtained(LevelScreenTwo screenTwo) {
						switchToNextLevelScreen(screenTwo, next, router);
					}

				});
	}

	protected void switchToNextLevelScreen(LevelScreenOne screen, Level next,
			Router router) {
		screen.setLevel(next);
		Transition transition = TransitionBuilder.buildNew().slideInRight()
				.interpolateClearColor().get();
		transition.setDuration(.6f);
		router.changeScreen(screen, transition);
	}
}
