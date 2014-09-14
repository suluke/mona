package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerAdapter;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.ui.screens.SplashScreen;
import de.lksbhm.mona.ui.screens.LevelScreenOne;

/**
 * Must not access anything that gets instantiated/loaded/set after
 * game.initialize
 *
 */
class DropInBehavior {
	private boolean loaded = false;

	public void load() {
		System.out.println("Load dropin behavior");
		loaded = true;
		loadTutorialsOnFirstStartBehavior();
	}

	private void loadTutorialsOnFirstStartBehavior() {
		LksBhmGame
				.getGame(Mona.class)
				.getContextManager()
				.addListener(
						new AbstractContextListenerAdapter<SplashScreen>(
								SplashScreen.class) {
							@Override
							protected void onEnterContext(
									final SplashScreen screen) {
								System.out.println("flashscreen");
								final Mona mona = LksBhmGame
										.getGame(Mona.class);
								final Level tutorialLevel = mona
										.getUserManager().getCurrentUser()
										.getTutorialLevelToPlay();
								if (tutorialLevel != null) {
									System.out.println("Play tutorial!");
									LevelScreenOne levelScreen = mona
											.getResourceConsumerManager()
											.obtainConsumerInstanceWithoutLoadingResources(
													LevelScreenOne.class);
									mona.getAssetManager().finishLoading();
									levelScreen.onResourcesLoaded(mona
											.getAssetManager());
									levelScreen.setLevel(tutorialLevel);
									screen.setNextScreen(levelScreen);
								}
							}

							@Override
							protected void onLeaveContext(SplashScreen screen) {
							}
						});
	}

	public boolean isLoaded() {
		return loaded;
	}
}
