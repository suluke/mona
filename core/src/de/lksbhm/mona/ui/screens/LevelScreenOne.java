package de.lksbhm.mona.ui.screens;

import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.mona.levels.Level;

public class LevelScreenOne extends AbstractLevelScreen {
	@Override
	protected void goToNextLevel(final Level next, final Router router) {
		router.obtainScreen(LevelScreenTwo.class,
				new ResourceConsumerObtainedCallback<LevelScreenTwo>() {
					@Override
					public void onObtained(LevelScreenTwo screenOne) {
						switchToNextLevelScreen(screenOne, next, router);
					}

				});
	}
}
