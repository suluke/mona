package de.lksbhm.mona.puzzle.representations.undirected;

public class TopBottom extends UndirectedTile {

	public TopBottom() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitTopBottom(this);
	}

	@Override
	public UndirectedTile getFirstAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile top = b.getTileOrNull(x, y - 1);
		return top;
	}

	@Override
	public UndirectedTile getSecondAdjacent() {
		UndirectedTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		UndirectedTile bottom = b.getTileOrNull(x, y + 1);
		return bottom;
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
	public TopBottom copy() {
		return UndirectedTileBoard.tbPool.obtain();
	}

	@Override
	public void dispose() {
		UndirectedTileBoard.tbPool.free(this);
	}

	@Override
	public TopBottom getHorizontalInverted() {
		return UndirectedTileBoard.tbPool.obtain();
	}

	@Override
	public TopBottom getVerticalInverted() {
		return UndirectedTileBoard.tbPool.obtain();
	}
}
