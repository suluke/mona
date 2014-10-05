package de.lksbhm.mona.levels;

import java.util.Iterator;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.gdx.util.ArrayIterator;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.User;

public abstract class LevelPackage implements Iterable<Level> {
	private final int size;
	private final Difficulty difficulty;
	private final String packageId;
	private String displayName;
	private final Level[] levels;
	private int levelsInArray = 0;
	private boolean loaded = false;
	private final LevelPackageCollection collection;

	public LevelPackage(String id, int size, Difficulty difficulty,
			LevelPackageCollection collection) {
		this.packageId = id;
		displayName = this.packageId;
		this.difficulty = difficulty;
		this.size = size;
		levels = new Level[size];
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
		levels[levelsInArray] = l;
		levelsInArray++;
	}

	@Override
	public Iterator<Level> iterator() {
		assertLevelsLoaded();
		return new ArrayIterator<Level>(levels);
	}

	private void assertLevelsLoaded() {
		if (!loaded) {
			// set loaded before actual load to prevent endless recursion
			loaded = true;
			loadLevels();
		}
		if (levelsInArray != size) {
			throw new RuntimeException();
		}
	}

	public Level getLevelAfter(Level level) {
		assertLevelsLoaded();
		for (int i = 0; i < size; i++) {
			if (levels[i].equals(level)) {
				if (i + 1 >= size) {
					return null;
				} else {
					return levels[i + 1];
				}
			}
		}
		return levels[0];
	}

	public LevelPackageCollection getLevelPackageCollection() {
		return collection;
	}

	public Level getLevel(int index) {
		assertLevelsLoaded();
		return levels[index];
	}

	public boolean isSolved() {
		boolean solved = LksBhmGame.getGame(Mona.class).getUserManager()
				.getCurrentUser().isPackageSolved(this);
		return solved;
	}

	public void notifySolved() {
		UserManager<User> userManager = LksBhmGame.getGame(Mona.class)
				.getUserManager();
		User user = userManager.getCurrentUser();
		user.addReward(getReward());
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getReward() {
		switch (difficulty) {
		case VERY_EASY:
			return 1;
		case EASY:
			return 2;
		case MEDIUM:
			return 3;
		case HARD:
			return 4;
		case VERY_HARD:
			return 5;
		default:
			return 0;
		}
	}
}
