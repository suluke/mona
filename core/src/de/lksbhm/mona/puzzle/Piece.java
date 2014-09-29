package de.lksbhm.mona.puzzle;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.Tile;

public class Piece extends Tile<Piece> {
	private Direction in = Direction.NONE;
	private Direction out = Direction.NONE;
	private Type type = Type.EMPTY;

	public static enum Type {
		EMPTY, EDGE, STRAIGHT, INVISIBLE;

		public static Type fromString(String string) {
			if ("EMPTY".equalsIgnoreCase(string)) {
				return EMPTY;
			} else if (("EDGE").equalsIgnoreCase(string)) {
				return EDGE;
			} else if ("STRAIGHT".equalsIgnoreCase(string)) {
				return STRAIGHT;
			} else if ("INVISIBLE".equalsIgnoreCase(string)) {
				return INVISIBLE;
			}
			throw new IllegalArgumentException("No such tile type: " + string);
		}

		public boolean mustBeInPath() {
			switch (this) {
			case EDGE:
			case STRAIGHT:
				return true;
			case EMPTY:
			case INVISIBLE:
				return false;
			default:
				return false;
			}
		}
	}

	@Override
	public Piece getNeighbor(Direction d) {
		Piece neighbor = super.getNeighbor(d);
		if (neighbor != null && neighbor.getType() == Type.INVISIBLE) {
			return null;
		} else {
			return neighbor;
		}
	}

	void setup(Board<Piece> b, int x, int y, Type t, Direction in, Direction out) {
		super.setup(b, x, y);
		this.type = t;
		setInOutDirection(in, out, false);
	}

	public void setInOutDirection(Direction in, Direction out, boolean notify) {
		if (in == out && in != Direction.NONE) {
			throw new RuntimeException(
					"In and out directions must not be equal");
		}
		if (type == Type.INVISIBLE
				&& (in != Direction.NONE || out != Direction.NONE)) {
			throw new RuntimeException(
					"Try to set in/out connection of invisible tile to something different than NONE");
		}
		if (this.in != in || this.out != out) {
			this.in = in;
			this.out = out;
			if (notify) {
				notifyOnChange();
			}
		}
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
	public boolean pushInOutDirection(Direction d, boolean notify) {
		if (type == Type.INVISIBLE && d != Direction.NONE) {
			throw new RuntimeException(
					"Try to set in/out connection of invisible tile to something different than NONE");
		}
		// don't set d twice
		if (in == d || out == d) {
			// nothing changed
			return false;
		}
		// drop previous 'out' in favor of (newer) 'in', except for when 'out'
		// is continued and 'in' is not
		if (!isOutContinued() || isInContinued()) {
			// prevent evicting 'out' with Direction.NONE
			if (in != Direction.NONE) {
				out = in;
			}
		}
		in = d;
		if (notify) {
			notifyOnChange();
		}
		return true;
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

	public void connectNeighbor(Direction d, boolean notify) {
		boolean changed = pushInOutDirection(d, false);
		changed |= getNeighbor(d).pushInOutDirection(d.getOpposite(), false);
		if (notify && changed) {
			notifyOnChange();
		}
	}

	public void connectNeighbor(Piece neighbor, boolean notify) {
		Direction dir = getDirectionOfNeighbor(neighbor);
		connectNeighbor(dir, notify);
	}

	public void disconnectNeighbor(Piece other, boolean notify) {
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
		if (notify) {
			notifyOnChange();
		}
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
		if (!type.mustBeInPath()
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
		if (thisIn == null || thisOut == null) {
			return false;
		}
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
		case INVISIBLE:
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
		return (in.in.isOrthogonalTo(in.out) && out.in.isOppositeOf(out.out))
				|| (in.in.isOppositeOf(in.out) && out.in
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
		if (type == Type.INVISIBLE) {
			setInOutDirection(Direction.NONE, Direction.NONE, false);
		}
	}

	protected void notifyOnChange() {
		getBoard().notifyOnChange();
	}

	@Override
	protected Piece[] allocateArray(int size) {
		return new Piece[size];
	}
}