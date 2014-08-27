package de.lksbhm.mona.puzzle;

import java.util.ArrayList;

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
		return true;
	}

	@Override
	public void notifyOnChange() {
		for (PuzzleChangedListener listener : listeners) {
			listener.onChange();
		}
	}

	public void addChangeListener(PuzzleChangedListener listener) {
		listeners.add(listener);
	}

	public void removeChangeListener(PuzzleChangedListener listener) {
		listeners.remove(listener);
	}

	public void removeAllChangeListeners() {
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
}
