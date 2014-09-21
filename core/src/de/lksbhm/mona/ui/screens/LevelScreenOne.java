package de.lksbhm.mona.ui.screens;


public class LevelScreenOne extends AbstractLevelScreen<LevelScreenTwo> {
	@Override
	protected Class<LevelScreenTwo> getNextLevelScreenTypeClass() {
		return LevelScreenTwo.class;
	}
}
