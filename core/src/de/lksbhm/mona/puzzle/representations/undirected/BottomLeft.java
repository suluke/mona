package de.lksbhm.mona.puzzle.representations.undirected;

public class BottomLeft extends UndirectedTile {

	public BottomLeft() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitBottomLeft(this);
	}

	@Override
	public UndirectedTile getFirstAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile bottom = b.getTileOrNull(x, y + 1);
		return bottom;
	}

	@Override
	public UndirectedTile getSecondAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile left = b.getTileOrNull(x - 1, y);
		return left;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isStraight() {
		return false;
	}

	@Override
	public BottomLeft copy() {
		return UndirectedTileBoard.blPool.obtain();
	}

	@Override
	public void dispose() {
		UndirectedTileBoard.blPool.free(this);
	}

	@Override
	public BottomRight getHorizontalInverted() {
		return UndirectedTileBoard.brPool.obtain();
	}

	@Override
	public TopLeft getVerticalInverted() {
		return UndirectedTileBoard.tlPool.obtain();
	}
}
