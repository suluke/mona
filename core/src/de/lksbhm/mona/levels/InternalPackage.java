package de.lksbhm.mona.levels;

import com.badlogic.gdx.files.FileHandle;

public class InternalPackage extends LevelPackage {
	private final FileHandle baseDir;

	InternalPackage(String id, int size, Difficulty difficulty,
			FileHandle baseDir) {
		super(id, size, difficulty);
		this.baseDir = baseDir;
	}

	@Override
	protected void loadLevels() {
		// TODO Auto-generated method stub

	}
}
