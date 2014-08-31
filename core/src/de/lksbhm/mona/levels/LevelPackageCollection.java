package de.lksbhm.mona.levels;

import java.util.Iterator;

import de.lksbhm.gdx.util.ArrayIterator;

public class LevelPackageCollection implements Iterable<LevelPackage> {
	private final LevelPackage[] packages;

	public LevelPackageCollection(int size) {
		packages = new LevelPackage[size];
	}

	public void setPackage(int index, LevelPackage p) {
		if (index < 0 || index >= packages.length) {
			throw new IndexOutOfBoundsException();
		}
		packages[index] = p;
	}

	public LevelPackage getPackage(int index) {
		if (index < 0 || index >= packages.length) {
			throw new IndexOutOfBoundsException();
		}
		return packages[index];
	}

	@Override
	public Iterator<LevelPackage> iterator() {
		return new ArrayIterator<LevelPackage>(packages);
	}

	public int size() {
		return packages.length;
	}
}
