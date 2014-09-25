package de.lksbhm.mona.puzzle.representations.linked;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LinkedTileBoard extends Board<LinkedTile> implements Disposable {
	static final Pool<LinkedTile> directedNodePool = new Pool<LinkedTile>() {
		@Override
		protected LinkedTile newObject() {
			return new LinkedTile();
		}

	};

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
		super(width, height);
		if (initializeTiles) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					setTile(obtainNode(x, y), x, y);
				}
			}
		}
	}

	public DirectionalTileBoard toUndirected() {
		DirectionalTileBoard result = new DirectionalTileBoard(getWidth(),
				getHeight());

		for (LinkedTile linked : this) {
			linkedToDirectionalNode(linked, result);
		}
		return result;
	}

	private void linkedToDirectionalNode(LinkedTile node, DirectionalTileBoard b) {
		Direction first;
		Direction second;
		int x = node.getX();
		int y = node.getY();
		LinkedTile parent = node.getParent();
		if (parent == null) {
			b.setTileToNoDirection(x, y);
			return;
		}
		first = node.getDirectionOfNeighbor(parent);
		LinkedTile child = node.getChild();
		second = node.getDirectionOfNeighbor(child);

		if (second.isGreater(first)) {
			Direction swap = first;
			first = second;
			second = swap;
		}
		switch (first) {
		case UP: {
			switch (second) {
			case DOWN: {
				b.setTileToTopBottom(x, y);
				break;
			}
			case LEFT: {
				b.setTileToTopLeft(x, y);
				break;
			}
			case RIGHT: {
				b.setTileToTopRight(x, y);
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
				b.setTileToBottomLeft(x, y);
				break;
			}
			case RIGHT: {
				b.setTileToBottomRight(x, y);
				break;
			}
			default:
				throw new RuntimeException();
			}
			break;
		}
		case LEFT: {
			if (second == Direction.RIGHT) {
				b.setTileToLeftRight(x, y);
			} else {
				throw new RuntimeException();
			}
			break;
		}
		default:
			throw new RuntimeException();
		}
	}

	@Override
	public void dispose() {
		for (LinkedTile node : this) {
			directedNodePool.free(node);
		}
	}

	@Override
	public LinkedTileBoard shallowCopy() {
		LinkedTileBoard copy = (LinkedTileBoard) super.shallowCopy();
		int width = getWidth();
		int height = getHeight();
		LinkedTile currentTile;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				currentTile = getTile(x, y);
				copy.getTile(x, y).setChild(
						copy.getTile(currentTile.getChild().getX(), currentTile
								.getChild().getY()));
				copy.getTile(x, y).setParent(
						copy.getTile(currentTile.getParent().getX(),
								currentTile.getParent().getY()));
			}
		}
		return copy;
	}

	@Override
	protected Board<LinkedTile> instantiate(int width, int height) {
		return new LinkedTileBoard(width, height, false);
	}

	@Override
	public String toString() {
		String lineSeparator = LksBhmGame.getGame(Mona.class)
				.getPlatformManager().getPlatform().getLineSeparator();
		StringBuilder builder = new StringBuilder();
		int height = getHeight();
		int width = getWidth();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				LinkedTile node = getTile(x, y);
				if (node.getParent() == null) {
					builder.append(' ');
				} else {
					builder.append('O');
				}
			}
			if (y != height - 1) {
				builder.append(lineSeparator);
			}
		}
		return builder.toString();
	}

	@Override
	protected LinkedTile[] createNodeArray(int size) {
		return new LinkedTile[size];
	}
}
