package de.lksbhm.mona.tutorials;

import com.badlogic.gdx.utils.Disposable;

import de.lksbhm.mona.levels.Level;

public abstract class Tutorial implements Disposable {
	private final Level level;

	public Tutorial(Level level) {
		this.level = level;
	}

	public abstract void load();

	public Level getLevel() {
		return level;
	}
}
