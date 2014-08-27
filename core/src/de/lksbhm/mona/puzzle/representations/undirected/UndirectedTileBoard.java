package de.lksbhm.mona.puzzle.representations.undirected;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTile;
import de.lksbhm.mona.puzzle.representations.grouped.GroupedTileBoard;
import de.lksbhm.mona.puzzle.representations.grouped.TileGroupType;

public class UndirectedTileBoard extends Board<UndirectedTile> implements
		Disposable {

	static final Pool<UndirectedEmpty> emptyPool = new Pool<UndirectedEmpty>() {
		@Override
		protected UndirectedEmpty newObject() {
			return new UndirectedEmpty();
		}
	};
	static final Pool<TopLeft> tlPool = new Pool<TopLeft>() {
		@Override
		protected TopLeft newObject() {
			return new TopLeft();
		}
	};
	static final Pool<TopRight> trPool = new Pool<TopRight>() {
		@Override
		protected TopRight newObject() {
			return new TopRight();
		}
	};
	static final Pool<BottomLeft> blPool = new Pool<BottomLeft>() {
		@Override
		protected BottomLeft newObject() {
			return new BottomLeft();
		}
	};
	static final Pool<BottomRight> brPool = new Pool<BottomRight>() {
		@Override
		protected BottomRight newObject() {
			return new BottomRight();
		}
	};
	static final Pool<TopBottom> tbPool = new Pool<TopBottom>() {
		@Override
		protected TopBottom newObject() {
			return new TopBottom();
		}
	};
	static final Pool<LeftRight> lrPool = new Pool<LeftRight>() {
		@Override
		protected LeftRight newObject() {
			return new LeftRight();
		}
	};

	public UndirectedTileBoard(int width, int height) {
		super(width, height, UndirectedTile.class);
	}

	public final void toRect() {
		int width = getWidth();
		int height = getHeight();
		UndirectedTile[][] nodes = getTiles();
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

	private UndirectedEmpty obtainEmpty(int x, int y) {
		UndirectedEmpty e = emptyPool.obtain();
		e.setup(this, x, y);
		return e;
	}

	private TopLeft obtainTL(int x, int y) {
		TopLeft tl = tlPool.obtain();
		tl.setup(this, x, y);
		return tl;
	}

	private TopRight obtainTR(int x, int y) {
		TopRight tr = trPool.obtain();
		tr.setup(this, x, y);
		return tr;
	}

	private TopBottom obtainTB(int x, int y) {
		TopBottom tb = tbPool.obtain();
		tb.setup(this, x, y);
		return tb;
	}

	private BottomLeft obtainBL(int x, int y) {
		BottomLeft bl = blPool.obtain();
		bl.setup(this, x, y);
		return bl;
	}

	private BottomRight obtainBR(int x, int y) {
		BottomRight br = brPool.obtain();
		br.setup(this, x, y);
		return br;
	}

	private LeftRight obtainLR(int x, int y) {
		LeftRight lr = lrPool.obtain();
		lr.setup(this, x, y);
		return lr;
	}

	@Override
	public void dispose() {
		UndirectedTileVisitor freer = new UndirectedTileVisitor() {

			@Override
			public void visitTopRight(TopRight topRight) {
				trPool.free(topRight);
			}

			@Override
			public void visitTopLeft(TopLeft topLeft) {
				tlPool.free(topLeft);
			}

			@Override
			public void visitTopBottom(TopBottom topBottom) {
				tbPool.free(topBottom);
			}

			@Override
			public void visitLeftRight(LeftRight leftRight) {
				lrPool.free(leftRight);
			}

			@Override
			public void visitEmpty(UndirectedEmpty empty) {
				emptyPool.free(empty);
			}

			@Override
			public void visitBottomRight(BottomRight bottomRight) {
				brPool.free(bottomRight);
			}

			@Override
			public void visitBottomLeft(BottomLeft bottomLeft) {
				blPool.free(bottomLeft);
			}
		};
		UndirectedTile[][] nodes = getTiles();
		for (UndirectedTile[] arr : nodes) {
			for (UndirectedTile node : arr) {
				if (node != null) {
					node.acceptVisitor(freer);
				}
			}
		}
	}

	@Override
	public UndirectedTile getTile(int x, int y) {
		return super.getTile(x, y);
	}

	@Override
	public UndirectedTile getTileOrNull(int x, int y) {
		return super.getTileOrNull(x, y);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final char[] character = new char[1];
		UndirectedTileVisitor visitor = new UndirectedTileVisitor() {

			@Override
			public void visitTopRight(TopRight topRight) {
				character[0] = '└'; // '┌', '┐', '└', '┘', '-', '|'
			}

			@Override
			public void visitTopLeft(TopLeft topLeft) {
				character[0] = '┘';
			}

			@Override
			public void visitTopBottom(TopBottom topBottom) {
				character[0] = '|';
			}

			@Override
			public void visitLeftRight(LeftRight leftRight) {
				character[0] = '-';
			}

			@Override
			public void visitEmpty(UndirectedEmpty empty) {
				character[0] = ' ';
			}

			@Override
			public void visitBottomRight(BottomRight bottomRight) {
				character[0] = '┌';
			}

			@Override
			public void visitBottomLeft(BottomLeft bottomLeft) {
				character[0] = '┐';
			}
		};
		UndirectedTile[][] nodes = getTiles();
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
		UndirectedTile[][] nodes = getTiles();
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
	protected Board<UndirectedTile> instantiate(int width, int height) {
		return new UndirectedTileBoard(width, height);
	}

	@Override
	public UndirectedTileBoard shallowCopyHorizontalFlipped() {
		UndirectedTileBoard copy = (UndirectedTileBoard) super
				.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		UndirectedTile[][] tiles = copy.getTiles();
		UndirectedTile current;
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
	public UndirectedTileBoard shallowCopyVerticalFlipped() {
		UndirectedTileBoard copy = (UndirectedTileBoard) super
				.shallowCopyHorizontalFlipped();
		int width = getWidth();
		int height = getHeight();
		UndirectedTile[][] tiles = copy.getTiles();
		UndirectedTile current;
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
