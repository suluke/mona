package de.lksbhm.mona.puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.undirected.UndirectedTileBoard;

public class Puzzle extends Board<Piece> implements Disposable {
	static final Pool<Piece> fieldPool = new ReflectionPool<Piece>(Piece.class);
	private final UndirectedTileBoard solution;
	private final ArrayList<PuzzleChangedListener> listeners = new ArrayList<PuzzleChangedListener>();
	private final HashSet<Piece> circleRoots = new HashSet<Piece>();
	private final boolean[][] isInvalid = new boolean[getWidth()][getHeight()];

	public Puzzle(UndirectedTileBoard solution, int width, int height) {
		this(solution, width, height, true);
	}

	private Puzzle(UndirectedTileBoard solution, int width, int height,
			boolean initializeTiles) {
		super(width, height, Piece.class);
		this.solution = solution;
		if (initializeTiles) {
			Piece f;
			Piece[][] nodes = getTiles();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					f = fieldPool.obtain();
					nodes[x][y] = f;
					f.setup(this, x, y, Type.EMPTY, Direction.NONE,
							Direction.NONE);
				}
			}
		}
	}

	@Override
	public void dispose() {
		Piece[][] nodes = getTiles();
		solution.dispose();
		for (Piece[] fieldArray : nodes) {
			for (Piece f : fieldArray) {
				fieldPool.free(f);
			}
		}
	}

	/**
	 * @return the solution
	 */
	public UndirectedTileBoard getSolution() {
		return solution;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Piece[][] pieces = getTiles();
		int width = getWidth();
		int height = getHeight();
		Piece p;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				p = pieces[x][y];
				switch (p.getType()) {
				case EDGE:
					sb.append('#');
					break;
				case EMPTY:
					sb.append(' ');
					break;
				case STRAIGHT:
					sb.append('+');
					break;
				default:
					throw new RuntimeException();
				}
			}
			if (y != height - 1) {
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public boolean looksEqualTo(Puzzle other) {
		int width = getWidth();
		int height = getHeight();
		if (other.getWidth() != width || other.getHeight() != height) {
			return false;
		}
		Piece[][] tiles = getTiles();
		Piece[][] otherTiles = other.getTiles();
		Piece current;
		Piece otherCurrent;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = tiles[x][y];
				otherCurrent = otherTiles[x][y];
				if (current.getType() != otherCurrent.getType()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isSolved() {
		Piece[][] tiles = getTiles();
		int width = getWidth();
		int height = getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!tiles[x][y].isValid()) {
					return false;
				}
			}
		}
		return isAllConnectedInOneCircle();
	}

	private boolean isAllConnectedInOneCircle() {
		resetIsValidArray();
		// find a tile that is not empty
		Piece root = null;
		for (Piece tile : this) {
			if (tile.getType() != Type.EMPTY) {
				root = tile;
				break;
			}
		}
		if (root == null) {
			// very strange
			return true;
		}
		Piece current = root;
		Piece previous = null;
		Piece adjacent;
		// mark all in circle, starting from our root
		do {
			adjacent = current.getInAdjacent();
			if (previous == adjacent && previous != null) {
				adjacent = current.getOutAdjacent();
			}
			previous = current;
			current = adjacent;
			if (current == null) {
				return false;
			}
			if (!previous.isConnectedWith(current)) {
				return false;
			}
			isInvalid[current.getX()][current.getY()] = true;
		} while (current != root);
		// iterate through all and see if there is a non-empty still not marked
		for (Piece tile : this) {
			if (tile.getType() != Type.EMPTY) {
				if (!isInvalid[tile.getX()][tile.getY()]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void notifyOnChange() {
		for (PuzzleChangedListener listener : listeners) {
			listener.onChange();
		}
	}

	public void addChangeListener(PuzzleChangedListener listener) {
		if (listener.getPuzzle() != null) {
			if (listener.getPuzzle() == this) {
				return;
			} else {
				listener.unregister();
			}
		}
		listeners.add(listener);
	}

	public void removeChangeListener(PuzzleChangedListener listener) {
		listener.setPuzzle(null);
		listeners.remove(listener);
	}

	public void removeAllChangeListeners() {
		for (PuzzleChangedListener listener : listeners) {
			listener.setPuzzle(null);
		}
		listeners.clear();
	}

	@Override
	protected Board<Piece> instantiate(int width, int height) {
		return new Puzzle(null, getWidth(), getHeight(), false);
	}

	@Override
	public Puzzle shallowCopyHorizontalFlipped() {
		Puzzle copy = (Puzzle) super.shallowCopyHorizontalFlipped();
		Piece[][] tiles = copy.getTiles();
		int width = getWidth();
		int height = getHeight();
		Piece current;
		Direction newIn;
		Direction newOut;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = tiles[x][y];
				newIn = current.getInDirection();
				newOut = current.getOutDirection();
				if (newIn == Direction.LEFT || newIn == Direction.RIGHT) {
					newIn = newIn.getOpposite();
				}
				if (newOut == Direction.LEFT || newOut == Direction.RIGHT) {
					newOut = newOut.getOpposite();
				}
				current.setInOutDirection(newIn, newOut);
			}
		}
		return copy;
	}

	@Override
	public Puzzle shallowCopyVerticalFlipped() {
		Puzzle copy = (Puzzle) super.shallowCopyHorizontalFlipped();
		Piece[][] tiles = copy.getTiles();
		int width = getWidth();
		int height = getHeight();
		Piece current;
		Direction newIn;
		Direction newOut;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = tiles[x][y];
				newIn = current.getInDirection();
				newOut = current.getOutDirection();
				if (newIn == Direction.UP || newIn == Direction.DOWN) {
					newIn = newIn.getOpposite();
				}
				if (newOut == Direction.UP || newOut == Direction.DOWN) {
					newOut = newOut.getOpposite();
				}
				current.setInOutDirection(newIn, newOut);
			}
		}
		return copy;
	}

	public void updateInvalidListTiles(LinkedList<Piece> list) {
		resetIsValidArray();

		Iterator<Piece> previouslyInvalid = list.iterator();
		Piece current;
		while (previouslyInvalid.hasNext()) {
			current = previouslyInvalid.next();
			if (current.isValid()) {
				previouslyInvalid.remove();
			} else {
				isInvalid[current.getX()][current.getY()] = true;
			}
		}
		Piece[][] tiles = getTiles();
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				if (!tile.isValid()) {
					if (!isInvalid[tile.getX()][tile.getY()]) {
						list.add(tile);
					}
				}
			}
		}
		circleRoots.clear();
		boolean isNewCircle;
		Piece firstCircle = null; // stupid compiler needs initialization...
		for (Piece[] array : tiles) {
			for (Piece tile : array) {
				if (tile.isValid() && tile.getType() != Type.EMPTY) {
					isNewCircle = isNewCircle(tile);
					if (isNewCircle) {
						if (circleRoots.size() == 1) {
							firstCircle = tile;
						} else {
							if (circleRoots.size() == 2) {
								addInvalidCircleToList(firstCircle, list);
							}
							addInvalidCircleToList(tile, list);
						}
					}
				}
			}
		}
	}

	private void resetIsValidArray() {
		int width = getWidth();
		int height = getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				isInvalid[x][y] = false;
			}
		}
	}

	/**
	 * Very unsafe method. Only use if you know that tile is in a circle. Better
	 * don't use it and have it only for updateInvalidListTiles
	 * 
	 * @param tile
	 * @param list
	 */
	private void addInvalidCircleToList(Piece tile, LinkedList<Piece> list) {
		Piece current = tile;
		Piece previous = null;
		Piece adjacent;
		do {
			adjacent = current.getInAdjacent();
			if (previous == adjacent && previous != null) {
				adjacent = current.getOutAdjacent();
			}
			previous = current;
			current = adjacent;
			if (!isInvalid[current.getX()][current.getY()]) {
				isInvalid[current.getX()][current.getY()] = true;
				list.add(current);
			}
		} while (current != tile);
	}

	private boolean isNewCircle(Piece tile) {
		Piece current = tile;
		Piece previous = null;
		Piece adjacent;
		do {
			adjacent = current.getInAdjacent();
			if (previous == adjacent && previous != null) {
				adjacent = current.getOutAdjacent();
			}
			previous = current;
			current = adjacent;
			if (current == null) {
				return false;
			}
			if (circleRoots.contains(current)) {
				return false;
			}
			if (!previous.isConnectedWith(current)) {
				return false;
			}
		} while (current != tile);
		circleRoots.add(tile);
		return true;
	}
}
