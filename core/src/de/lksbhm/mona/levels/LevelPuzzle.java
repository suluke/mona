package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LevelPuzzle extends Puzzle {

	private final Level level;

	public LevelPuzzle(Level level, DirectionalTileBoard solution, int width,
			int height) {
		super(solution, width, height);
		this.level = level;
	}

	@Override
	protected void onSolved() {
		level.notifySolved();
	}
}
