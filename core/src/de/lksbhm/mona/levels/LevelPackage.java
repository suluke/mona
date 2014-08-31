package de.lksbhm.mona.levels;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class LevelPackage implements Iterable<Level> {
	private int size;
	private Difficulty difficulty;
	private String packageId;
	private final ArrayList<Level> levels = new ArrayList<Level>();

	public int getSize() {
		return size;
	}

	public String getPackageId() {
		return packageId;
	}

	void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	@Override
	public Iterator<Level> iterator() {
		return levels.iterator();
	}

	void addLevel() {

	}
}
