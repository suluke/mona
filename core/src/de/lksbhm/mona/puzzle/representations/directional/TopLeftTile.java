package de.lksbhm.mona.puzzle.representations.directional;

public class TopLeftTile extends DirectionalTile {

	public TopLeftTile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitTopLeft(this);
	}

	@Override
	public DirectionalTile getFirstAdjacent() {
		DirectionalTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		DirectionalTile top = b.getTileOrNull(x, y - 1);
		return top;
	}

	@Override
	public DirectionalTile getSecondAdjacent() {
		DirectionalTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		DirectionalTile left = b.getTileOrNull(x - 1, y);
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
	public TopLeftTile copy() {
		return DirectionalTileBoard.tlPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.tlPool.free(this);
	}

	@Override
	public TopRightTile getHorizontalInverted() {
		return DirectionalTileBoard.trPool.obtain();
	}

	@Override
	public BottomLeftTile getVerticalInverted() {
		return DirectionalTileBoard.blPool.obtain();
	}
}
