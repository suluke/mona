package de.lksbhm.mona.puzzle.representations;

public enum Direction {
	UP, DOWN, LEFT, RIGHT, NONE;

	public boolean isOppositeOf(Direction d) {
		if (this == NONE || d == NONE) {
			return true;
		}
		if (d == this) {
			return false;
		}
		return d == this.getOpposite();
	}

	public boolean isNextTo(Direction d) {
		if (this == NONE || d == NONE) {
			return true;
		}
		if (d == this) {
			return false;
		}
		return d != this.getOpposite();
	}

	public Direction getOpposite() {
		if (this == NONE) {
			throw new RuntimeException("No opposite of NONE");
		}
		switch (this) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * Implements an ordering upon the direction, where NONE is always greater,
	 * followed by UP which is greater than DOWN which in turn is greater than
	 * LEFT. RIGHT is the "smallest" direction.
	 * 
	 * @param d
	 * @return
	 */
	public boolean isGreater(Direction d) {
		switch (this) {
		case NONE:
			return true;
		case UP:
			if (d == NONE) {
				return false;
			}
			return true;
		case DOWN:
			if (d == NONE || d == UP) {
				return false;
			}
			return true;
		case LEFT:
			if (d == LEFT || d == RIGHT) {
				return true;
			}
			return false;
		case RIGHT:
			if (d == RIGHT) {
				return true;
			}
			return false;
		}
		throw new RuntimeException();
	}
}
