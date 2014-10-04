package de.lksbhm.mona.ui.screens;

public class RandomPuzzleScreenOne extends
		AbstractRandomPuzzleScreen<RandomPuzzleScreenTwo> {

	@Override
	protected Class<RandomPuzzleScreenTwo> getNextScreenTypeClass() {
		return RandomPuzzleScreenTwo.class;
	}
}
