package de.lksbhm.mona.puzzle;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.Tile;

public class Piece extends Tile<Piece> {
	private Direction in = Direction.NONE;
	private Direction out = Direction.NONE;
	private Type type = Type.EMPTY;

	Piece() {
		super(Piece.class);
	}

	void setup(Board<Piece> b, int x, int y, Type t, Direction in, Direction out) {
		super.setup(b, x, y);
		this.type = t;
		setInOutDirection(in, out);
	}

	public void setInOutDirection(Direction in, Direction out) {
		if (in == out && in != Direction.NONE) {
			throw new RuntimeException(
					"In and out directions must not be equal");
		}
		this.in = in;
		this.out = out;
	}

	public Direction getInDirection() {
		return in;
	}

	public Direction getOutDirection() {
		return out;
	}

	/**
	 * Implements a small queue somehow, so that the more recently changed in or
	 * out direction should be out
	 * 
	 * @param d
	 */
	public void pushInOutDirection(Direction d) {
		out = in;
		in = d;
	}

	public Piece getInAdjacent() {
		return getNeighbor(in);
	}

	public Piece getOutAdjacent() {
		return getNeighbor(out);
	}

	public static enum Type {
		EMPTY, EDGE, STRAIGHT,
	}

	public Type getType() {
		return type;
	}

	public boolean isValid() {
		boolean valid = true;
		// empty fields have not constraints
		if (type == Type.EMPTY) {
			return true;
		}
		// pieces with just one in/out set are invalid
		if ((in == Direction.NONE && out != Direction.NONE)
				|| (in != Direction.NONE && out == Direction.NONE)) {
			return false;
		}
		// if not touched, it is valid
		if (in == Direction.NONE && out == Direction.NONE) {
			return true;
		}
		Piece thisIn = getNeighbor(in);
		Piece thisOut = getNeighbor(out);
		switch (type) {
		case EDGE:
			valid = validateEdge(thisIn, thisOut);
			break;
		case STRAIGHT:
			valid = validateStraight(thisIn, thisOut);
		default:
			throw new RuntimeException();
		}

		return valid;
	}

	private boolean validateEdge(Piece in, Piece out) {
		if (this.in.isOppositeOf(this.out)) {
			return false;
		}
		return in.in.isOppositeOf(in.out) && out.in.isOppositeOf(out.out);
	}

	private boolean validateStraight(Piece in, Piece out) {
		if (this.in.isNextTo(this.out)) {
			return false;
		}
		return in.in.isNextTo(in.out) || out.in.isNextTo(out.out);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
}
