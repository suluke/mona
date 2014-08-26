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
	private static final Pool<Piece> fieldPool = new ReflectionPool<Piece>(
			Piece.class);
	private final UndirectedTileBoard solution;
	private final ArrayList<PuzzleChangedListener> listeners = new ArrayList<PuzzleChangedListener>();

	public Puzzle(UndirectedTileBoard solution, int width, int height) {
		super(width, height, Piece.class);
		this.solution = solution;
		Piece f;
		Piece[][] nodes = getTiles();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				f = fieldPool.obtain();
				nodes[x][y] = f;
				f.setup(this, x, y, Type.EMPTY, Direction.NONE, Direction.NONE);
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
}
