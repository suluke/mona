package de.lksbhm.mona.puzzle.representations.undirected;

public class BottomRight extends UndirectedTile {

	public BottomRight() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitBottomRight(this);
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
		UndirectedTile right = b.getTileOrNull(x + 1, y);
		return right;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isStraight() {
		return false;
	}
}
