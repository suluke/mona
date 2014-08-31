package de.lksbhm.mona.levels;

import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.ui.screens.AbstractScreen;

public abstract class Level extends ContextImplementation {
	private final boolean solved = false;

	private AbstractScreen view;

	public void setView(AbstractScreen view) {
		this.view = view;
	}

	public AbstractScreen getView() {
		return view;
	}

	public abstract Puzzle getPuzzle();

	public boolean isSolved() {
		return solved;
	}

	public abstract String getPackageId();

	public abstract String getLevelId();
}
