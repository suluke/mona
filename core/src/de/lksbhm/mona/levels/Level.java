package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.Puzzle;

public abstract class Level {
	private final boolean solved = false;

	public abstract Puzzle instantiatePuzzle();

	public boolean isSolved() {
		return solved;
	}
}
