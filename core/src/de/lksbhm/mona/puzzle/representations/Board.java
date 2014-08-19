package de.lksbhm.mona.puzzle.representations;

import java.lang.reflect.Array;

public abstract class Board<TileBaseType extends Tile<TileBaseType>> {
	private final TileBaseType[][] nodes;
	private final int width;
	private final int height;

	@SuppressWarnings("unchecked")
	public Board(int width, int height,
			Class<? extends TileBaseType> nodeBaseType) {
		nodes = (TileBaseType[][]) Array.newInstance(nodeBaseType, width,
				height);
		this.width = width;
		this.height = height;
	}

	public TileBaseType getTile(int x, int y) {
		TileBaseType n = getTileOrNull(x, y);
		if (n == null) {
			throw new RuntimeException("No node in field with width " + width
					+ " and height " + height + " at position " + x + ", " + y);
		}
		return n;
	}

	public TileBaseType getTileOrNull(int x, int y) {
		if (x < 0 || x >= width) {
			return null;
		}
		if (y < 0 || y >= height) {
			return null;
		}
		return nodes[x][y];
	}

	public TileBaseType[][] getTiles() {
		return nodes;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
