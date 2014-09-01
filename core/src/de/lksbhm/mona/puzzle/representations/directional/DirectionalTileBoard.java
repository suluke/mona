package de.lksbhm.mona.puzzle.representations.directional;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTile;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTileBoard;
import de.lksbhm.mona.puzzle.representations.grouped.TileGroupType;

public class DirectionalTileBoard extends Board<DirectionalTile> implements
		Disposable {

	static final Pool<NoDirectionTile> emptyPool = new Pool<NoDirectionTile>() {
		@Override
		protected NoDirectionTile newObject() {
			return new NoDirectionTile();
		}
	};
	static final Pool<TopLeftTile> tlPool = new Pool<TopLeftTile>() {
		@Override
		protected TopLeftTile newObject() {
			return new TopLeftTile();
		}
	};
	static final Pool<TopRightTile> trPool = new Pool<TopRightTile>() {
		@Override
		protected TopRightTile newObject() {
			return new TopRightTile();
		}
	};
	static final Pool<BottomLeftTile> blPool = new Pool<BottomLeftTile>() {
		@Override
		protected BottomLeftTile newObject() {
			return new BottomLeftTile();
		}
	};
	static final Pool<BottomRightTile> brPool = new Pool<BottomRightTile>() {
		@Override
		protected BottomRightTile newObject() {
			return new BottomRightTile();
		}
	};
	static final Pool<TopBottomTile> tbPool = new Pool<TopBottomTile>() {
		@Override
		protected TopBottomTile newObject() {
			return new TopBottomTile();
		}
	};
	static final Pool<LeftRightTile> lrPool = new Pool<LeftRightTile>() {
		@Override
		protected LeftRightTile newObject() {
			return new LeftRightTile();
		}
	};

	public DirectionalTileBoard(int width, int height) {
		super(width, height, DirectionalTile.class);
	}

	public final void toRect() {
		int width = getWidth();
		int height = getHeight();
		DirectionalTile[][] nodes = getTiles();
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				nodes[x][y] = obtainEmpty(x, y);
			}
		}
		for (int x = 1; x < width - 1; x++) {
			nodes[x][0] = obtainLR(x, 0);
			nodes[x][height - 1] = obtainLR(x, height - 1);
		}
		for (int y = 1; y < height - 1; y++) {
			nodes[0][y] = obtainTB(0, y);
			nodes[width - 1][y] = obtainTB(width - 1, y);
		}
		nodes[0][0] = obtainBR(0, 0);
		nodes[width - 1][height - 1] = obtainTL(width - 1, height - 1);
		nodes[width - 1][0] = obtainBL(width - 1, 0);
		nodes[0][height - 1] = obtainTR(0, height - 1);
	}

	private NoDirectionTile obtainEmpty(int x, int y) {
		NoDirectionTile e = emptyPool.obtain();
		e.setup(this, x, y);
		return e;
	}

	private TopLeftTile obtainTL(int x, int y) {
		TopLeftTile tl = tlPool.obtain();
		tl.setup(this, x, y);
		return tl;
	}

	private TopRightTile obtainTR(int x, int y) {
		TopRightTile tr = trPool.obtain();
		tr.setup(this, x, y);
		return tr;
	}

	private TopBottomTile obtainTB(int x, int y) {
		TopBottomTile tb = tbPool.obtain();
		tb.setup(this, x, y);
		return tb;
	}

	private BottomLeftTile obtainBL(int x, int y) {
		BottomLeftTile bl = blPool.obtain();
		bl.setup(this, x, y);
		return bl;
	}

	private BottomRightTile obtainBR(int x, int y) {
		BottomRightTile br = brPool.obtain();
		br.setup(this, x, y);
		return br;
	}

	private LeftRightTile obtainLR(int x, int y) {
		LeftRightTile lr = lrPool.obtain();
		lr.setup(this, x, y);
		return lr;
	}

	@Override
	public void dispose() {
		DirectionalTileVisitor freer = new DirectionalTileVisitor() {

			@Override
			public void visitTopRight(TopRightTile topRight) {
				trPool.free(topRight);
			}

			@Override
			public void visitTopLeft(TopLeftTile topLeft) {
				tlPool.free(topLeft);
			}

			@Override
			public void visitTopBottom(TopBottomTile topBottom) {
				tbPool.free(topBottom);
			}

			@Override
			public void visitLeftRight(LeftRightTile leftRight) {
				lrPool.free(leftRight);
			}

			@Override
			public void visitEmpty(NoDirectionTile empty) {
				emptyPool.free(empty);
			}

			@Override
			public void visitBottomRight(BottomRightTile bottomRight) {
				brPool.free(bottomRight);
			}

			@Override
			public void visitBottomLeft(BottomLeftTile bottomLeft) {
				blPool.free(bottomLeft);
			}
		};
		DirectionalTile[][] nodes = getTiles();
		for (DirectionalTile[] arr : nodes) {
			for (DirectionalTile node : arr) {
				if (node != null) {
					node.acceptVisitor(freer);
				}
			}
		}
	}

	@Override
	public DirectionalTile getTile(int x, int y) {
		return super.getTile(x, y);
	}

	@Override
	public DirectionalTile getTileOrNull(int x, int y) {
		return super.getTileOrNull(x, y);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final char[] character = new char[1];
		DirectionalTileVisitor visitor = new DirectionalTileVisitor() {

			@Override
			public void visitTopRight(TopRightTile topRight) {
				character[0] = '└'; // '┌', '┐', '└', '┘', '-', '|'
			}

			@Override
			public void visitTopLeft(TopLeftTile topLeft) {
				character[0] = '┘';
			}

			@Override
			public void visitTopBottom(TopBottomTile topBottom) {
				character[0] = '|';
			}

			@Override
			public void visitLeftRight(LeftRightTile leftRight) {
				character[0] = '-';
			}

			@Override
			public void visitEmpty(NoDirectionTile empty) {
				character[0] = ' ';
			}

			@Override
			public void visitBottomRight(BottomRightTile bottomRight) {
				character[0] = '┌';
			}

			@Override
			public void visitBottomLeft(BottomLeftTile bottomLeft) {
				character[0] = '┐';
			}
		};
		DirectionalTile[][] nodes = getTiles();
		int width = getWidth();
		int height = getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				nodes[x][y].acceptVisitor(visitor);
				sb.append(character[0]);
			}
			if (y != height - 1) {
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public GroupedTileBoard toGroupedTileBoard() {
		DirectionalTile[][] nodes = getTiles();
		GroupedTileBoard result = new GroupedTileBoard(getWidth(), getHeight());
		GroupedTile[][] groupNodes = result.getTiles();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				groupNodes[x][y].setType(TileGroupType
						.fromUndirectedNode(nodes[x][y]));
			}
		}
		return result;
	}

	@Override
	protected Board<DirectionalTile> instantiate(int width, int height) {
		return new DirectionalTileBoard(width, height);
	}

	@Override
	public DirectionalTileBoard shallowCopy() {
		return (DirectionalTileBoard) super.shallowCopy();
	}

	@Override
	public DirectionalTileBoard shallowCopyHorizontalFlipped() {
		DirectionalTileBoard copy = (DirectionalTileBoard) super
				.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		DirectionalTile[][] tiles = copy.getTiles();
		DirectionalTile current;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = tiles[x][y];
				tiles[x][y] = current.getHorizontalInverted();
				current.dispose();
			}
		}
		return copy;
	}

	@Override
	public DirectionalTileBoard shallowCopyVerticalFlipped() {
		DirectionalTileBoard copy = (DirectionalTileBoard) super
				.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		DirectionalTile[][] tiles = copy.getTiles();
		DirectionalTile current;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = tiles[x][y];
				tiles[x][y] = current.getVerticalInverted();
				current.dispose();
			}
		}
		return copy;
	}
}