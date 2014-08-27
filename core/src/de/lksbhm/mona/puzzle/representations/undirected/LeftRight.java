package de.lksbhm.mona.puzzle.representations.undirected;

public class LeftRight extends UndirectedTile {

	public LeftRight() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitLeftRight(this);
	}

	@Override
	public UndirectedTile getFirstAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile left = b.getTileOrNull(x - 1, y);
		return left;
	}

	@Override
	public UndirectedTile getSecondAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile right = b.getTileOrNull(x + 1, y);
		return right;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isStraight() {
		return true;
	}

	@Override
	public LeftRight copy() {
		return UndirectedTileBoard.lrPool.obtain();
	}

	@Override
	public void dispose() {
		UndirectedTileBoard.lrPool.free(this);
	}

	@Override
	public LeftRight getHorizontalInverted() {
		return UndirectedTileBoard.lrPool.obtain();
	}

	@Override
	public LeftRight getVerticalInverted() {
		return UndirectedTileBoard.lrPool.obtain();
	}
}
