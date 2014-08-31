package de.lksbhm.mona.puzzle.representations.directional;

public class LeftRightTile extends DirectionalTile {

	public LeftRightTile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitLeftRight(this);
	}

	@Override
	public DirectionalTile getFirstAdjacent() {
		DirectionalTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		DirectionalTile left = b.getTileOrNull(x - 1, y);
		return left;
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
		return true;
	}

	@Override
	public LeftRightTile copy() {
		return DirectionalTileBoard.lrPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.lrPool.free(this);
	}

	@Override
	public LeftRightTile getHorizontalInverted() {
		return DirectionalTileBoard.lrPool.obtain();
	}

	@Override
	public LeftRightTile getVerticalInverted() {
		return DirectionalTileBoard.lrPool.obtain();
	}
}
