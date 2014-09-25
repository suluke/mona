package de.lksbhm.mona.puzzle.representations.directional;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.gdx.LksBhmGame;
import de.lksbhm.mona.Mona;
import de.lksbhm.mona.puzzle.representations.Board;
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
		super(width, height);
	}

	public final void toRect() {
		int width = getWidth();
		int height = getHeight();
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				setTile(obtainNoDirection(x, y), x, y);
			}
		}
		for (int x = 1; x < width - 1; x++) {
			setTile(obtainLR(x, 0), x, 0);
			setTile(obtainLR(x, height - 1), x, height - 1);
		}
		for (int y = 1; y < height - 1; y++) {
			setTile(obtainTB(0, y), 0, y);
			setTile(obtainTB(width - 1, y), width - 1, y);
		}
		setTile(obtainBR(0, 0), 0, 0);
		setTile(obtainTL(width - 1, height - 1), width - 1, height - 1);
		setTile(obtainBL(width - 1, 0), width - 1, 0);
		setTile(obtainTR(0, height - 1), 0, height - 1);
	}

	private NoDirectionTile obtainNoDirection(int x, int y) {
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

	public void setTileToNoDirection(int x, int y) {
		setTile(x, y, obtainNoDirection(x, y));
	}

	public void setTileToTopLeft(int x, int y) {
		setTile(x, y, obtainTL(x, y));
	}

	public void setTileToTopRight(int x, int y) {
		setTile(x, y, obtainTR(x, y));
	}

	public void setTileToTopBottom(int x, int y) {
		setTile(x, y, obtainTB(x, y));
	}

	public void setTileToBottomLeft(int x, int y) {
		setTile(x, y, obtainBL(x, y));
	}

	public void setTileToBottomRight(int x, int y) {
		setTile(x, y, obtainBR(x, y));
	}

	public void setTileToLeftRight(int x, int y) {
		setTile(x, y, obtainLR(x, y));
	}

	private void setTile(int x, int y, DirectionalTile tile) {
		if (!isInBounds(x, y)) {
			throw new RuntimeException();
		}
		DirectionalTile previous = getTileOrNull(x, y);
		if (previous != null) {
			previous.dispose();
		}
		setTile(tile, x, y);
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
		for (DirectionalTile node : this) {
			if (node != null) {
				node.acceptVisitor(freer);
			}
		}
	}

	public GroupedTileBoard toGroupedTileBoard() {
		GroupedTileBoard result = new GroupedTileBoard(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				result.getTile(x, y).setType(
						TileGroupType.fromUndirectedNode(getTile(x, y)));
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
		DirectionalTile current;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = copy.getTile(x, y);
				copy.setTile(current.getHorizontalInverted(), x, y);
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
		DirectionalTile current;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				current = copy.getTile(x, y);
				copy.setTile(current.getVerticalInverted(), x, y);
				current.dispose();
			}
		}
		return copy;
	}

	@Override
	public String toString() {
		String lineSeparator = LksBhmGame.getGame(Mona.class)
				.getPlatformManager().getPlatform().getLineSeparator();
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
		int width = getWidth();
		int height = getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				getTile(x, y).acceptVisitor(visitor);
				sb.append(character[0]);
			}
			if (y != height - 1) {
				sb.append(lineSeparator);
			}
		}
		return sb.toString();
	}

	@Override
	protected DirectionalTile[] createNodeArray(int size) {
		return new DirectionalTile[size];
	}
}
