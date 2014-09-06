package de.lksbhm.mona.ui.screens;

import de.lksbhm.gdx.Router;
import de.lksbhm.gdx.resources.ResourceConsumerObtainedCallback;
import de.lksbhm.mona.levels.Level;

public class LevelScreenTwo extends LevelScreenOne {
	@Override
	protected void goToNextLevel(final Level next, final Router router) {
		router.obtainScreen(LevelScreenOne.class,
				new ResourceConsumerObtainedCallback<LevelScreenOne>() {
					@Override
					public void onObtained(LevelScreenOne screenOne) {
						switchToNextLevelScreen(screenOne, next, router);
					}

				});
	}
}
