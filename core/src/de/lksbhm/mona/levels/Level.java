package de.lksbhm.mona.levels;

import com.badlogic.gdx.utils.Disposable;

import de.lksbhm.gdx.contexts.ContextImplementation;
import de.lksbhm.mona.puzzle.Puzzle;

/**
 * Levels always belong to a package, otherwise they would just be mere puzzles.
 *
 */
public abstract class Level extends ContextImplementation implements Disposable {

	private final String id;
	private final LevelPackage pack;
	private final boolean solved = false;
	private Puzzle p;

	public Level(LevelPackage pack, String id) {
		this.pack = pack;
		this.id = id;
	}

	public boolean isSolved() {
		return solved;
	}

	public Puzzle getPuzzle() {
		if (p == null) {
			p = instantiatePuzzle();
		}
		return p;
	}

	protected abstract Puzzle instantiatePuzzle();

	public String getLevelId() {
		return id;
	}

	public LevelPackage getPackage() {
		return pack;
	}

	public void reset() {
		if (p != null) {
			p.clearInOuDirections();
		}
	}

	@Override
	public void dispose() {
		if (p != null) {
			p.dispose();
			p = null;
		}
	}

	public Level getNext() {
		// TODO
		return null;
	}
}
