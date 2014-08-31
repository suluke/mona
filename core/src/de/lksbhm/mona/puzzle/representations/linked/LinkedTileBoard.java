package de.lksbhm.mona.puzzle.representations.linked;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.directional.BottomLeftTile;
import de.lksbhm.mona.puzzle.representations.directional.BottomRightTile;
import de.lksbhm.mona.puzzle.representations.directional.LeftRightTile;
import de.lksbhm.mona.puzzle.representations.directional.TopBottomTile;
import de.lksbhm.mona.puzzle.representations.directional.TopLeftTile;
import de.lksbhm.mona.puzzle.representations.directional.TopRightTile;
import de.lksbhm.mona.puzzle.representations.directional.NoDirectionTile;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTile;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LinkedTileBoard extends Board<LinkedTile> implements Disposable {
	static final Pool<LinkedTile> directedNodePool = new ReflectionPool<LinkedTile>(
			LinkedTile.class);

	private LinkedTile obtainNode(int x, int y) {
		LinkedTile n = directedNodePool.obtain();
		n.setup(this, x, y);
		n.setParent(null);
		n.setChild(null);
		return n;
	}

	public LinkedTileBoard(int width, int height) {
		this(width, height, true);
	}

	private LinkedTileBoard(int width, int height, boolean initializeTiles) {
		super(width, height, LinkedTile.class);
		if (initializeTiles) {
			LinkedTile[][] nodes = getTiles();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					nodes[x][y] = obtainNode(x, y);
				}
			}
		}
	}

	@Override
	public LinkedTile getTile(int x, int y) {
		return super.getTile(x, y);
	}

	@Override
	public LinkedTile getTileOrNull(int x, int y) {
		return super.getTileOrNull(x, y);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		LinkedTile[][] nodes = getTiles();
		int height = nodes[0].length;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < nodes.length; x++) {
				LinkedTile node = nodes[x][y];
				if (node.getParent() == null) {
					builder.append(' ');
				} else {
					builder.append('O');
				}
			}
			if (y != height - 1) {
				builder.append(System.lineSeparator());
			}
		}
		return builder.toString();
	}

	public DirectionalTileBoard toUndirected() {
		DirectionalTileBoard result = new DirectionalTileBoard(getWidth(),
				getHeight());
		LinkedTile[][] directedNodes = getTiles();
		DirectionalTile[][] undirectedNodes = result.getTiles();
		int x;
		int y;
		for (LinkedTile[] array : directedNodes) {
			for (LinkedTile directed : array) {
				x = directed.getX();
				y = directed.getY();
				undirectedNodes[x][y] = undirectedFromDirectedNode(directed, x,
						y, result);
			}
		}
		return result;
	}

	private DirectionalTile undirectedFromDirectedNode(LinkedTile node, int x,
			int y, Board<DirectionalTile> b) {
		int nodeX = node.getX();
		int nodeY = node.getY();
		Direction first;
		Direction second;
		LinkedTile parent = node.getParent();
		if (parent == null) {
			NoDirectionTile empty = new NoDirectionTile();
			empty.setup(b, x, y);
			return empty;
		}
		LinkedTile child = node.getChild();
		if (parent.getX() == nodeX) {
			if (parent.getY() < nodeY) {
				first = Direction.UP;
			} else {
				first = Direction.DOWN;
			}
		} else {
			if (parent.getX() < nodeX) {
				first = Direction.LEFT;
			} else {
				first = Direction.RIGHT;
			}
		}
		if (child.getX() == nodeX) {
			if (child.getY() < nodeY) {
				second = Direction.UP;
			} else {
				second = Direction.DOWN;
			}
		} else {
			if (child.getX() < nodeX) {
				second = Direction.LEFT;
			} else {
				second = Direction.RIGHT;
			}
		}

		if (second.isGreater(first)) {
			Direction swap = first;
			first = second;
			second = swap;
		}
		DirectionalTile result;
		switch (first) {
		case UP: {
			switch (second) {
			case DOWN: {
				result = new TopBottomTile();
				break;
			}
			case LEFT: {
				result = new TopLeftTile();
				break;
			}
			case RIGHT: {
				result = new TopRightTile();
				break;
			}
			default:
				throw new RuntimeException();
			}
			break;
		}
		case DOWN: {
			switch (second) {
			case LEFT: {
				result = new BottomLeftTile();
				break;
			}
			case RIGHT: {
				result = new BottomRightTile();
				break;
			}
			default:
				throw new RuntimeException();
			}
			break;
		}
		case LEFT: {
			if (second == Direction.RIGHT) {
				result = new LeftRightTile();
			} else {
				throw new RuntimeException();
			}
			break;
		}
		default:
			throw new RuntimeException();
		}
		result.setup(b, x, y);
		return result;
	}

	@Override
	public void dispose() {
		LinkedTile[][] nodes = getTiles();
		for (LinkedTile[] array : nodes) {
			for (LinkedTile node : array) {
				directedNodePool.free(node);
			}
		}
	}

	@Override
	public LinkedTileBoard shallowCopy() {
		LinkedTileBoard copy = (LinkedTileBoard) super.shallowCopy();
		LinkedTile[][] tiles = getTiles();
		LinkedTile[][] copyTiles = copy.getTiles();
		int width = getWidth();
		int height = getHeight();
		LinkedTile currentTile;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				currentTile = tiles[x][y];
				copyTiles[x][y].setChild(copy.getTile(currentTile.getChild()
						.getX(), currentTile.getChild().getY()));
				copyTiles[x][y].setParent(copy.getTile(currentTile.getParent()
						.getX(), currentTile.getParent().getY()));
			}
		}
		return copy;
	}

	@Override
	protected Board<LinkedTile> instantiate(int width, int height) {
		return new LinkedTileBoard(width, height, false);
	}
}
