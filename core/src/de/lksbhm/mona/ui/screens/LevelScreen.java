package de.lksbhm.mona.ui.screens;

import de.lksbhm.mona.levels.Level;

public class LevelScreen extends AbstractPuzzleScreen {
	private Level l;

	public void setLevel(Level l) {
		this.l = l;
		l.reset();
		setPuzzle(l.getPuzzle());
	}

	@Override
	public boolean isRequestingLoadingAnimation() {
		return false;
	}

	@Override
	protected void onWin() {

	}
}
