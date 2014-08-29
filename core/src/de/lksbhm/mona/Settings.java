package de.lksbhm.mona;

import de.lksbhm.mona.levels.Difficulty;

public class Settings implements de.lksbhm.gdx.Settings {
	public int getMinPuzzleWidthForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 2;
		case EASY:
			return 4;
		case MEDIUM:
			return 6;
		case HARD:
			return 8;
		case VERY_HARD:
			return 10;
		default:
			throw new RuntimeException();
		}
	}

	public int getMaxPuzzleWidthForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 3;
		case EASY:
			return 5;
		case MEDIUM:
			return 7;
		case HARD:
			return 9;
		case VERY_HARD:
			return 11;
		default:
			throw new RuntimeException();
		}
	}

	public int getMinPuzzleHeightForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 3;
		case EASY:
			return 5;
		case MEDIUM:
			return 7;
		case HARD:
			return 9;
		case VERY_HARD:
			return 11;
		default:
			throw new RuntimeException();
		}
	}

	public int getMaxPuzzleHeightForDifficulty(Difficulty d) {
		switch (d) {
		case VERY_EASY:
			return 4;
		case EASY:
			return 6;
		case MEDIUM:
			return 8;
		case HARD:
			return 10;
		case VERY_HARD:
			return 12;
		default:
			throw new RuntimeException();
		}
	}
}
