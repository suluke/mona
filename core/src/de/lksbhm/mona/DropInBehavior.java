package de.lksbhm.mona;

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
		Mona.getGame()
				.getContextManager()
				.addListener(
						new AbstractContextListenerAdapter<SplashScreen>(
								SplashScreen.class) {
							@Override
							protected void onEnterContext(
									final SplashScreen screen) {
								final Mona mona = Mona.getGame();
								final Level firstLevel = mona
										.getLevelPackageManager()
										.getInternalPackages().getPackage(0)
										.getLevel(0);
								if (!firstLevel.isSolved()) {
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
