package de.lksbhm.mona.levels;

import java.util.ArrayList;
import java.util.Iterator;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.gdx.users.UserManager;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.User;

public abstract class LevelPackage implements Iterable<Level> {
	private final int size;
	private final Difficulty difficulty;
	private final String packageId;
	private String displayName;
	private final ArrayList<Level> levels = new ArrayList<Level>();
	private boolean loaded = false;
	private final LevelPackageCollection collection;

	public LevelPackage(String id, int size, Difficulty difficulty,
			LevelPackageCollection collection) {
		this.packageId = id;
		displayName = this.packageId;
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

	public Level getLevel(int index) {
		assertLevelsLoaded();
		return levels.get(index);
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
		switch (difficulty) {
		case VERY_EASY:
			user.addReward(1);
			break;
		case EASY:
			user.addReward(2);
			break;
		case MEDIUM:
			user.addReward(3);
			break;
		case HARD:
			user.addReward(4);
			break;
		case VERY_HARD:
			user.addReward(5);
			break;
		default:
			break;
		}
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
