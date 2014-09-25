package de.lksbhm.mona.puzzle.representations;

import java.util.Iterator;

import com.badlogic.gdx.utils.Disposable;

public abstract class Board<TileBaseType extends Tile<TileBaseType>> implements
		Iterable<TileBaseType>, Disposable {
	private final TileBaseType[] nodes;
	private final int width;
	private final int height;

	public Board(int width, int height) {
		if (width == 0 || height == 0) {
			throw new RuntimeException();
		}
		this.width = width;
		this.height = height;
		nodes = createNodeArray(width * height);
	}

	protected abstract TileBaseType[] createNodeArray(int size);

	public TileBaseType getTile(int x, int y) {
		TileBaseType n = getTileOrNull(x, y);
		if (n == null) {
			throw new RuntimeException("No node in field with width " + width
					+ " and height " + height + " at position " + x + ", " + y);
		}
		return n;
	}

	public void setTile(TileBaseType tile, int x, int y) {
		nodes[y * width + x] = tile;
	}

	public TileBaseType getTileOrNull(int x, int y) {
		if (!isInBounds(x, y)) {
			return null;
		}
		return nodes[y * width + x];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isInBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
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
		TileBaseType previousTile;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				previousTile = copy.getTileOrNull(x, y);
				if (previousTile != null) {
					previousTile.dispose();
				}
				copy.setTile(getTile(x, y).copy(), x, y);
			}
		}
		return copy;
	}

	public Board<TileBaseType> shallowCopyHorizontalFlipped() {
		Board<TileBaseType> copy = shallowCopy();
		TileBaseType swap;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height / 2; y++) {
				swap = copy.getTile(x, y);
				copy.setTile(copy.getTile(x, height - y - 1), x, y);
				copy.setTile(swap, x, height - y - 1);
			}
		}
		return copy;
	}

	public Board<TileBaseType> shallowCopyVerticalFlipped() {
		Board<TileBaseType> copy = shallowCopy();
		TileBaseType swap;
		for (int x = 0; x < width / 2; x++) {
			for (int y = 0; y < height; y++) {
				swap = copy.getTile(x, y);
				copy.setTile(copy.getTile(width - x - 1, y), x, y);
				copy.setTile(swap, width - x - 1, y);
			}
		}
		return copy;
	}

	@Override
	public void dispose() {
		for (TileBaseType tile : this) {
			tile.dispose();
		}
	}

	@Override
	public Iterator<TileBaseType> iterator() {
		return new Iterator<TileBaseType>() {
			private int x = 0;
			private int y = 0;

			@Override
			public boolean hasNext() {
				return y < height;
			}

			@Override
			public TileBaseType next() {
				if (!hasNext()) {
					throw new IndexOutOfBoundsException();
				}
				TileBaseType tile = getTile(x, y);
				x++;
				if (x == width) {
					y++;
					x = 0;
				}
				return tile;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
