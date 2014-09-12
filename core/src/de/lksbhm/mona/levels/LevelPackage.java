package de.lksbhm.mona.levels;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class LevelPackage implements Iterable<Level> {
	private final int size;
	private final Difficulty difficulty;
	private final String packageId;
	private final ArrayList<Level> levels = new ArrayList<Level>();
	private boolean loaded = false;
	private final LevelPackageCollection collection;

	public LevelPackage(String id, int size, Difficulty difficulty,
			LevelPackageCollection collection) {
		this.packageId = id;
		this.difficulty = difficulty;
		this.size = size;
		this.collection = collection;
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

	public Level getLevelAfter(Level level) {
		assertLevelsLoaded();
		if (levels.contains(level)) {
			for (int i = 0; i < size - 1; i++) {
				if (levels.get(i).equals(level)) {
					return levels.get(i + 1);
				}
			}
			return null;
		} else {
			return levels.get(0);
		}
	}

	public LevelPackageCollection getLevelPackageCollection() {
		return collection;
	}
}
