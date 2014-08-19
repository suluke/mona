package de.lksbhm.mona.puzzle.representations.undirected;

public class UndirectedEmpty extends UndirectedTile {

	@Override
	public void acceptVisitor(UndirectedTileVisitor visitor) {
		visitor.visitEmpty(this);
	}

	@Override
	public UndirectedTile getFirstAdjacent() {
		return null;
	}

	@Override
	public UndirectedTile getSecondAdjacent() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean isStraight() {
		return false;
	}

}
