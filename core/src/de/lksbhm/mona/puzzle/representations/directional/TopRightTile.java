package de.lksbhm.mona.puzzle.representations.directional;

public class TopRightTile extends DirectionalTile {

	TopRightTile() {
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitTopRight(this);
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
		DirectionalTile right = b.getTileOrNull(x + 1, y);
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

	@Override
	public TopRightTile copy() {
		return DirectionalTileBoard.trPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.trPool.free(this);
	}

	@Override
	public TopLeftTile getHorizontalInverted() {
		return DirectionalTileBoard.tlPool.obtain();
	}

	@Override
	public BottomRightTile getVerticalInverted() {
		return DirectionalTileBoard.brPool.obtain();
	}
}
