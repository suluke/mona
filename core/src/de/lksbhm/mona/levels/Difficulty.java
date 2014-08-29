package de.lksbhm.mona.levels;

public enum Difficulty {
	VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD;
	public static Difficulty fromNumber(int n) {
		if (n < 0) {
			n = 0;
		}
		if (n > 4) {
			n = 4;
		}
		switch (n) {
		case 0:
			return VERY_EASY;
		case 1:
			return EASY;
		case 2:
			return MEDIUM;
		case 3:
			return HARD;
		case 4:
			return VERY_HARD;

		default:
			throw new RuntimeException();
		}
	}
}
