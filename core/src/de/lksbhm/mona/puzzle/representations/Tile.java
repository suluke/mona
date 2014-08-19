package de.lksbhm.mona.puzzle.representations;

import java.lang.reflect.Array;

public abstract class Tile<NodeBaseType extends Tile<NodeBaseType>> {
	private int x, y;
	private Board<NodeBaseType> b;
	private final Class<? extends NodeBaseType> nodeBaseType;

	public Tile(Class<? extends NodeBaseType> nodeBaseType) {
		this.nodeBaseType = nodeBaseType;
	}

	public void setup(Board<NodeBaseType> b, int x, int y) {
		if (x < 0 || x >= b.getWidth()) {
			throw new IllegalArgumentException();
		}
		if (y < 0 || y >= b.getHeight()) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
		this.b = b;
	}

	public NodeBaseType[] getNeightbors() {
		int neighborCount = 0;
		int width = b.getWidth();
		int height = b.getHeight();
		NodeBaseType top = null;
		if (y > 0) {
			neighborCount++;
			top = b.getTile(x, y - 1);
		}
		NodeBaseType bottom = null;
		if (y < height - 1) {
			neighborCount++;
			bottom = b.getTile(x, y + 1);
		}
		NodeBaseType left = null;
		if (x > 0) {
			neighborCount++;
			left = b.getTile(x - 1, y);
		}
		NodeBaseType right = null;
		if (x < width - 1) {
			neighborCount++;
			right = b.getTile(x + 1, y);
		}
		@SuppressWarnings("unchecked")
		NodeBaseType[] neighbors = (NodeBaseType[]) Array.newInstance(
				nodeBaseType, neighborCount);
		int pos = 0;
		if (top != null) {
			neighbors[pos++] = top;
		}
		if (bottom != null) {
			neighbors[pos++] = bottom;
		}
		if (left != null) {
			neighbors[pos++] = left;
		}
		if (right != null) {
			neighbors[pos++] = right;
		}
		return neighbors;
	}

	public NodeBaseType getNeighbor(Direction d) {
		NodeBaseType[][] fields = b.getTiles();
		int x = getX();
		int y = getY();
		int w = fields.length;
		int h = fields[0].length;
		switch (d) {
		case NONE:
			return null;
		case UP:
			if (y > 0) {
				return fields[x][y - 1];
			}
			break;
		case LEFT:
			if (x > 0) {
				return fields[x - 1][y];
			}
			break;
		case DOWN:
			if (y < h - 1) {
				return fields[x][y + 1];
			}
			break;
		case RIGHT:
			if (x < w - 1) {
				return fields[x + 1][y];
			}
			break;
		default:
			throw new RuntimeException();
		}
		return null;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	protected Board<NodeBaseType> getBoard() {
		return b;
	}
}
