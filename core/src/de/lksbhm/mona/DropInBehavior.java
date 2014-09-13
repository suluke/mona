package de.lksbhm.mona;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.contexts.AbstractContextListenerHelper;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
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
						new AbstractContextListenerHelper<Mona>(Mona.class) {
							@Override
							protected void onEnterContext(final Mona mona) {
								if (!mona.getUserManager().getCurrentUser()
										.hasPlayedTutorials()) {
									System.out.println("Play tutorials!");
									mona.getRouter()
											.obtainScreen(
													LevelScreenOne.class,
													new ResourceConsumerObtainedCallback<LevelScreenOne>() {

														@Override
														public void onObtained(
																LevelScreenOne screen) {
															screen.setLevel(mona
																	.getLevelPackageManager()
																	.getInternalPackages()
																	.getPackage(
																			0)
																	.getLevel(0));
															mona.getRouter()
																	.changeScreen(
																			screen);
														}

													});
								}
							}

							@Override
							protected void onLeaveContext(Mona mona) {
							}
						});
	}

	public boolean isLoaded() {
		return loaded;
	}
}
