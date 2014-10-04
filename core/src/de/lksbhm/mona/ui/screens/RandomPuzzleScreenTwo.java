package de.lksbhm.mona.ui.screens;

public class RandomPuzzleScreenTwo extends
		AbstractRandomPuzzleScreen<RandomPuzzleScreenOne> {

	@Override
	protected Class<RandomPuzzleScreenOne> getNextScreenTypeClass() {
		return RandomPuzzleScreenOne.class;
	}
}
