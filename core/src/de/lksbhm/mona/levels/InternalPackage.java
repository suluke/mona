package de.lksbhm.mona.levels;

import java.io.File;
import java.io.FileFilter;

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
		// loadAllPossibleLevels();
		loadLevelsAccordingToSize();
	}

	@SuppressWarnings("unused")
	private void loadAllPossibleLevels() {
		FileHandle[] levelFiles = baseDir.list(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return false;
				}
				String name = file.getName();
				if (name.length() != 2 + 1 + 4) {
					return false;
				}
				if (Character.isDigit(name.charAt(0))
						&& Character.isDigit(name.charAt(1))) {
					return true;
				}
				return false;
			}
		});
		loadLevelFileArray(levelFiles);
	}

	private void loadLevelsAccordingToSize() {
		int size = getSize();
		FileHandle[] levelFiles = new FileHandle[size];
		FileHandle current;
		for (int i = 0; i < size; i++) {
			String name = String.format("%1$02d.json", i);
			current = baseDir.child(name);
			if (!current.exists()) {
				throw new RuntimeException();
			}
			if (current.isDirectory()) {
				throw new RuntimeException();
			}
			levelFiles[i] = current;
		}
		loadLevelFileArray(levelFiles);
	}

	private void loadLevelFileArray(FileHandle[] levelFiles) {
		Level current;
		for (FileHandle levelFile : levelFiles) {
			current = LevelLoadHelper.fromJsonForPackage(levelFile, this);
			putLevel(current);
		}
	}
}
