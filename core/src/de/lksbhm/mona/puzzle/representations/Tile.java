package de.lksbhm.mona.puzzle.representations;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ArrayReflection;

public abstract class Tile<TileBaseType extends Tile<TileBaseType>> implements
		Disposable {
	private int x, y;
	private Board<TileBaseType> b;
	private final Class<? extends TileBaseType> nodeBaseType;

	public Tile(Class<? extends TileBaseType> nodeBaseType) {
		this.nodeBaseType = nodeBaseType;
	}

	public void setup(Board<TileBaseType> b, int x, int y) {
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

	public TileBaseType[] getNeightbors() {
		int neighborCount = 0;
		int width = b.getWidth();
		int height = b.getHeight();
		TileBaseType top = null;
		if (y > 0) {
			neighborCount++;
			top = b.getTile(x, y - 1);
		}
		TileBaseType bottom = null;
		if (y < height - 1) {
			neighborCount++;
			bottom = b.getTile(x, y + 1);
		}
		TileBaseType left = null;
		if (x > 0) {
			neighborCount++;
			left = b.getTile(x - 1, y);
		}
		TileBaseType right = null;
		if (x < width - 1) {
			neighborCount++;
			right = b.getTile(x + 1, y);
		}
		@SuppressWarnings("unchecked")
		TileBaseType[] neighbors = (TileBaseType[]) ArrayReflection
				.newInstance(nodeBaseType, neighborCount);
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

	public boolean isNeighborOf(Tile<TileBaseType> tile) {
		if (tile == null) {
			return false;
		}
		return Math.abs(x - tile.x) + Math.abs(y - tile.y) < 2;
	}

	public Direction getDirectionOfNeighbor(Tile<TileBaseType> neighbor) {
		if (!isNeighborOf(neighbor)) {
			return Direction.NONE;
		}
		int nx = neighbor.getX();
		int ny = neighbor.getY();
		if (nx < x) {
			return Direction.LEFT;
		} else if (nx > x) {
			return Direction.RIGHT;
		} else if (ny < y) {
			return Direction.UP;
		} else if (ny > y) {
			return Direction.DOWN;
		}
		return Direction.NONE;
	}

	public TileBaseType getNeighbor(Direction d) {
		int x = getX();
		int y = getY();
		int w = b.getWidth();
		int h = b.getHeight();
		switch (d) {
		case NONE:
			return null;
		case UP:
			if (y > 0) {
				return b.getTile(x, y - 1);
			}
			break;
		case LEFT:
			if (x > 0) {
				return b.getTile(x - 1, y);
			}
			break;
		case DOWN:
			if (y < h - 1) {
				return b.getTile(x, y + 1);
			}
			break;
		case RIGHT:
			if (x < w - 1) {
				return b.getTile(x + 1, y);
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

	protected Board<TileBaseType> getBoard() {
		return b;
	}

	public abstract TileBaseType copy();
}
