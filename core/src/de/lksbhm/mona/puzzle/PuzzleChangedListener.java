package de.lksbhm.mona.puzzle;

public abstract class PuzzleChangedListener {
	private Puzzle p;

	Puzzle getPuzzle() {
		return p;
	}

	void setPuzzle(Puzzle p) {
		this.p = p;
	}

	public void register(Puzzle p) {
		p.addChangeListener(this);
	}

	public void unregister() {
		if (p != null) {
			p.removeChangeListener(this);
		}
	}

	public abstract void onChange();
}
