package de.lksbhm.mona.puzzle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Direction;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class Puzzle extends Board<Piece> implements Disposable {
	static final Pool<Piece> fieldPool = new Pool<Piece>() {
		@Override
		protected Piece newObject() {
			return new Piece();
		}

	};
	private final DirectionalTileBoard solution;
	private final LinkedList<PuzzleChangedListener> changeListeners = new LinkedList<PuzzleChangedListener>();
	private final LinkedList<PuzzleWonListener> winListeners = new LinkedList<PuzzleWonListener>();
	private final HashSet<Piece> circleRoots = new HashSet<Piece>();
	private final boolean[][] isInvalid = new boolean[getWidth()][getHeight()];
	private final LinkedList<Piece> invalidTiles = new LinkedList<Piece>();
	private boolean isSolved = false;

	public Puzzle(DirectionalTileBoard solution, int width, int height) {
		this(solution, width, height, true);
	}

	private Puzzle(DirectionalTileBoard solution, int width, int height,
			boolean initializeTiles) {
		super(width, height);
		this.solution = solution;
		if (initializeTiles) {
			Piece f;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					f = fieldPool.obtain();
					setTile(f, x, y);
					f.setup(this, x, y, Type.EMPTY, Direction.NONE,
							Direction.NONE);
				}
			}
		}
	}

	@Override
	public void dispose() {
		solution.dispose();
		for (Piece f : this) {
			fieldPool.free(f);
		}
	}

	/**
	 * @return the solution
	 */
	public DirectionalTileBoard getSolution() {
		return solution;
	}

	public boolean looksEqualTo(Puzzle other) {
		int width = getWidth();
		int height = getHeight();
		if (other.getWidth() != width || other.getHeight() != height) {
			return false;
		}
		Piece current;
		Piece otherCurrent;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = getTile(x, y);
				otherCurrent = other.getTile(x, y);
				if (current.getType() != otherCurrent.getType()) {
					return false;
				}
			}
		}
		return true;
	}

	private void updateSolvedState() {
		if (isSolved) {
			return;
		}
		for (Piece tile : this) {
			if (!tile.isValid()) {
				isSolved = false;
				return;
			}
		}
		isSolved = isAllConnectedInOneCircle();
		if (isSolved) {
			onSolved();
			for (PuzzleWonListener listener : winListeners) {
				listener.onWin();
			}
			winListeners.clear();
		}
	}

	protected void onSolved() {
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
		updateInvalidTilesList();
		updateSolvedState();
		for (PuzzleChangedListener listener : changeListeners) {
			listener.onChange();
		}
	}

	public void addWinListener(PuzzleWonListener listener) {
		if (!winListeners.contains(listener)) {
			winListeners.add(listener);
		}
	}

	public void removeWinListener(PuzzleWonListener listener) {
		winListeners.remove(listener);
	}

	public void addChangeListener(PuzzleChangedListener listener) {
		if (!changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}

	public void removeChangeListener(PuzzleChangedListener listener) {
		changeListeners.remove(listener);
	}

	public void removeAllChangeListeners() {
		changeListeners.clear();
	}

	private void updateInvalidTilesList() {
		LinkedList<Piece> list = invalidTiles;
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
		for (Piece tile : this) {
			if (!tile.isValid()) {
				if (!isInvalid[tile.getX()][tile.getY()]) {
					list.add(tile);
				}
			}
		}
		circleRoots.clear();
		boolean isNewCircle;
		Piece firstCircle = null; // stupid compiler needs initialization...
		for (Piece tile : this) {
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

	public Iterator<Piece> invalidTilesIterator() {
		return invalidTiles.iterator();
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

	public void reset() {
		changeListeners.clear();
		winListeners.clear();
		clearInOutDirections(false);
		invalidTiles.clear();
		isSolved = false;
	}

	public void clearInOutDirections(boolean notify) {
		for (Piece p : this) {
			p.setInOutDirection(Direction.NONE, Direction.NONE, notify);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int width = getWidth();
		int height = getHeight();
		Piece p;
		String lineSeparator = LksBhmGame.getGame(Mona.class)
				.getPlatformManager().getPlatform().getLineSeparator();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				p = getTile(x, y);
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
				sb.append(lineSeparator);
			}
		}
		return sb.toString();
	}

	@Override
	protected Board<Piece> instantiate(int width, int height) {
		return new Puzzle(null, getWidth(), getHeight(), false);
	}

	@Override
	public Puzzle shallowCopyHorizontalFlipped() {
		Puzzle copy = (Puzzle) super.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		Piece current;
		Direction newIn;
		Direction newOut;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = copy.getTile(x, y);
				newIn = current.getInDirection();
				newOut = current.getOutDirection();
				if (newIn == Direction.LEFT || newIn == Direction.RIGHT) {
					newIn = newIn.getOpposite();
				}
				if (newOut == Direction.LEFT || newOut == Direction.RIGHT) {
					newOut = newOut.getOpposite();
				}
				current.setInOutDirection(newIn, newOut, false);
			}
		}
		return copy;
	}

	@Override
	public Puzzle shallowCopyVerticalFlipped() {
		Puzzle copy = (Puzzle) super.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		Piece current;
		Direction newIn;
		Direction newOut;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = copy.getTile(x, y);
				newIn = current.getInDirection();
				newOut = current.getOutDirection();
				if (newIn == Direction.UP || newIn == Direction.DOWN) {
					newIn = newIn.getOpposite();
				}
				if (newOut == Direction.UP || newOut == Direction.DOWN) {
					newOut = newOut.getOpposite();
				}
				current.setInOutDirection(newIn, newOut, false);
			}
		}
		return copy;
	}

	@Override
	protected Piece[] createNodeArray(int size) {
		return new Piece[size];
	}
}
