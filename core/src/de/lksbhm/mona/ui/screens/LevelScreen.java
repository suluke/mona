package de.lksbhm.mona.ui.screens;

import de.lksbhm.mona.levels.Level;

public class LevelScreen extends PuzzleScreen {
	private Level l;

	public void setLevel(Level l) {
		this.l = l;
		setPuzzle(l.getPuzzle());
	}
}
