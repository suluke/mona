package de.lksbhm.mona.puzzle.representations.linked;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.undirected.BottomLeft;
import de.lksbhm.mona.puzzle.representations.undirected.BottomRight;
import de.lksbhm.mona.puzzle.representations.undirected.LeftRight;
import de.lksbhm.mona.puzzle.representations.undirected.TopBottom;
import de.lksbhm.mona.puzzle.representations.undirected.TopLeft;
import de.lksbhm.mona.puzzle.representations.undirected.TopRight;
import de.lksbhm.mona.puzzle.representations.undirected.UndirectedTileBoard;
import de.lksbhm.mona.puzzle.representations.undirected.UndirectedEmpty;
import de.lksbhm.mona.puzzle.representations.undirected.UndirectedTile;

public class LinkedTileBoard extends Board<LinkedTile> implements Disposable {
	private static final Pool<LinkedTile> directedNodePool = new ReflectionPool<LinkedTile>(
			LinkedTile.class);

	private LinkedTile obtainNode(int x, int y) {
		LinkedTile n = directedNodePool.obtain();
		n.setup(this, x, y);
		return n;
	}

	public LinkedTileBoard(int width, int height) {
		super(width, height, LinkedTile.class);
		LinkedTile[][] nodes = getTiles();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				nodes[x][y] = obtainNode(x, y);
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

	public UndirectedTileBoard toUndirected() {
		UndirectedTileBoard result = new UndirectedTileBoard(getWidth(), getHeight());
		LinkedTile[][] directedNodes = getTiles();
		UndirectedTile[][] undirectedNodes = result.getTiles();
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

	private UndirectedTile undirectedFromDirectedNode(LinkedTile node, int x,
			int y, Board<UndirectedTile> b) {
		int nodeX = node.getX();
		int nodeY = node.getY();
		Direction first;
		Direction second;
		LinkedTile parent = node.getParent();
		if (parent == null) {
			UndirectedEmpty empty = new UndirectedEmpty();
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
		UndirectedTile result;
		switch (first) {
		case UP: {
			switch (second) {
			case DOWN: {
				result = new TopBottom();
				break;
			}
			case LEFT: {
				result = new TopLeft();
				break;
			}
			case RIGHT: {
				result = new TopRight();
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
				result = new BottomLeft();
				break;
			}
			case RIGHT: {
				result = new BottomRight();
				break;
			}
			default:
				throw new RuntimeException();
			}
			break;
		}
		case LEFT: {
			if (second == Direction.RIGHT) {
				result = new LeftRight();
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
}
