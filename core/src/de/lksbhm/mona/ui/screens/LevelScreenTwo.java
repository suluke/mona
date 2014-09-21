package de.lksbhm.mona.ui.screens;


public class LevelScreenTwo extends AbstractLevelScreen<LevelScreenOne> {

	@Override
	protected Class<LevelScreenOne> getNextLevelScreenTypeClass() {
		return LevelScreenOne.class;
	}
}
