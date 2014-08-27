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

	/**
	 * Provides a global method that can be called by {@link Tile}
	 * implementations to notify the board about changes. The default
	 * implementation is empty, but inherited classes may override the method to
	 * for example provide listener capabilities.
	 */
	public void notifyOnChange() {

	}

	protected abstract Board<TileBaseType> instantiate(int width, int height);

	/**
	 * Shallow because it will only copy the nodes but not any additionally
	 * added attributes. Subtypes may override this method to a)
	 * 
	 * @return
	 */
	public Board<TileBaseType> shallowCopy() {
		Board<TileBaseType> copy = instantiate(width, height);
		TileBaseType[][] tiles = copy.nodes;
		TileBaseType previousTile;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				previousTile = tiles[x][y];
				if (previousTile != null) {
					previousTile.dispose();
				}
				tiles[x][y] = this.nodes[x][y].copy();
			}
		}
		return copy;
	}

	public Board<TileBaseType> shallowCopyHorizontalFlipped() {
		Board<TileBaseType> copy = shallowCopy();
		TileBaseType[][] tiles = copy.getTiles();
		TileBaseType swap;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height / 2; y++) {
				swap = tiles[x][y];
				tiles[x][y] = tiles[x][height - y - 1];
				tiles[x][height - y - 1] = swap;
			}
		}
		return copy;
	}

	public Board<TileBaseType> shallowCopyVerticalFlipped() {
		Board<TileBaseType> copy = shallowCopy();
		TileBaseType[][] tiles = copy.getTiles();
		TileBaseType swap;
		for (int x = 0; x < width / 2; x++) {
			for (int y = 0; y < height; y++) {
				swap = tiles[x][y];
				tiles[x][y] = tiles[width - x - 1][y];
				tiles[width - x - 1][y] = swap;
			}
		}
		return copy;
	}
}
