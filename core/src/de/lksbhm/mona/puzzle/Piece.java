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
		notifyOnChange();
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
		// don't set d twice
		if (in == d || out == d) {
			return;
		}
		// if 'out' is continued and 'in' is not, we prefer to keep 'out'
		if (!(isOutContinued() && !isInContinued())) {
			// prevent evicting 'out' with Direction.NONE
			if (in != Direction.NONE) {
				out = in;
			}
		}
		in = d;
		notifyOnChange();
	}

	public Piece getInAdjacent() {
		return getNeighbor(in);
	}

	public Piece getOutAdjacent() {
		return getNeighbor(out);
	}

	public boolean isInContinued() {
		if (in == Direction.NONE) {
			return false;
		}
		Piece neighbor = getNeighbor(in);
		if (neighbor == null) {
			return false;
		}
		return neighbor.in == in.getOpposite()
				|| neighbor.out == in.getOpposite();
	}

	public boolean isOutContinued() {
		if (out == Direction.NONE) {
			return false;
		}
		Piece neighbor = getNeighbor(out);
		if (neighbor == null) {
			return false;
		}
		return neighbor.in == out.getOpposite()
				|| neighbor.out == out.getOpposite();
	}

	public boolean isConnectedWith(Piece other) {
		Direction d = getDirectionOfNeighbor(other);
		if (d == Direction.NONE) {
			return false;
		}
		if (in == d || out == d) {
			d = d.getOpposite();
			if (other.in == d || other.out == d) {
				return true;
			}
		}
		return false;
	}

	public void disconnect(Piece other) {
		Direction d = getDirectionOfNeighbor(other);
		if (d == Direction.NONE) {
			return;
		}
		if (in == d) {
			in = Direction.NONE;
		}
		if (out == d) {
			out = Direction.NONE;
		}
		d = d.getOpposite();
		if (other.in == d) {
			other.in = Direction.NONE;
		}
		if (other.out == d) {
			other.out = Direction.NONE;
		}
		notifyOnChange();
	}

	public static enum Type {
		EMPTY, EDGE, STRAIGHT,
	}

	public Type getType() {
		return type;
	}

	public boolean isValid() {
		// pieces with just one in/out set are invalid
		if ((in == Direction.NONE && out != Direction.NONE)
				|| (in != Direction.NONE && out == Direction.NONE)) {
			return false;
		}
		// empties are valid if none of the connectors are set
		if (type == Type.EMPTY
				&& (in == Direction.NONE && out == Direction.NONE)) {
			return true;
		}
		// other field types must have both set, though
		if (in == Direction.NONE && out == Direction.NONE) {
			return false;
		}
		// we also need to check if the connections are being continued
		Piece thisIn = getNeighbor(in);
		Piece thisOut = getNeighbor(out);
		if (thisIn.in != in.getOpposite() && thisIn.out != in.getOpposite()) {
			return false;
		}
		if (thisOut.in != out.getOpposite() && thisOut.out != out.getOpposite()) {
			return false;
		}
		switch (type) {
		case EDGE:
			return validateEdge(thisIn, thisOut);
		case STRAIGHT:
			return validateStraight(thisIn, thisOut);
		case EMPTY:
			return true;
		default:
			throw new RuntimeException();
		}
	}

	private boolean validateEdge(Piece in, Piece out) {
		if (this.in.isOppositeOf(this.out)) {
			return false;
		}
		return in.in.isOppositeOf(in.out) && out.in.isOppositeOf(out.out);
	}

	private boolean validateStraight(Piece in, Piece out) {
		if (this.in.isOrthogonalTo(this.out)) {
			return false;
		}
		return (in.in.isOrthogonalTo(in.out) && !out.in.isOrthogonalTo(out.out))
				|| !(in.in.isOrthogonalTo(in.out) && out.in
						.isOrthogonalTo(out.out));
	}

	@Override
	public Piece copy() {
		Piece p = Puzzle.fieldPool.obtain();
		p.in = in;
		p.out = out;
		p.type = type;
		return p;
	}

	@Override
	public void dispose() {
		Puzzle.fieldPool.free(this);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	protected void notifyOnChange() {
		getBoard().notifyOnChange();
	}
}