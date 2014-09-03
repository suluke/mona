package de.lksbhm.mona.levels;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class LevelPackage implements Iterable<Level> {
	private final int size;
	private final Difficulty difficulty;
	private final String packageId;
	private final ArrayList<Level> levels = new ArrayList<Level>();
	private boolean loaded = false;

	public LevelPackage(String id, int size, Difficulty difficulty) {
		this.packageId = id;
		this.difficulty = difficulty;
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public String getPackageId() {
		return packageId;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * Lazy loading levels for this package
	 */
	protected abstract void loadLevels();

	protected void putLevel(Level l) {
		levels.add(l);
	}

	@Override
	public Iterator<Level> iterator() {
		assertLevelsLoaded();
		return levels.iterator();
	}

	private void assertLevelsLoaded() {
		if (!loaded) {
			loadLevels();
			loaded = true;
		}
	}
}
