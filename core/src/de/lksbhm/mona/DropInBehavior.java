package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerAdapter;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.ui.screens.LevelScreenOne;
import de.lksbhm.mona.ui.screens.SplashScreen;

/**
 * Must not access anything that gets instantiated/loaded/set after
 * game.initialize
 *
 */
class DropInBehavior {
	private boolean loaded = false;

	public void load() {
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
								final Mona mona = LksBhmGame
										.getGame(Mona.class);
								final Level firstLevel = mona
										.getLevelPackageManager()
										.getInternalPackages().getPackage(0)
										.getLevel(0);
								if (!firstLevel.isSolved()) {
									System.out.println("Play tutorial!");
									LevelScreenOne levelScreen = mona
											.getResourceConsumerManager()
											.obtainConsumerInstanceWithoutLoadingResources(
													LevelScreenOne.class);
									mona.getAssetManager().finishLoading();
									levelScreen.onResourcesLoaded(mona
											.getAssetManager());
									levelScreen.setLevel(firstLevel);
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
