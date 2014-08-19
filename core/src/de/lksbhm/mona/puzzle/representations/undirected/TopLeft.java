package de.lksbhm.mona.puzzle.representations.undirected;

public class TopLeft extends UndirectedTile {

	public TopLeft() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitTopLeft(this);
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
}
