package de.lksbhm.mona.puzzle.representations.directional;

public class BottomLeftTile extends DirectionalTile {

	BottomLeftTile() {
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitBottomLeft(this);
	}

	@Override
	public DirectionalTile getFirstAdjacent() {
		DirectionalTileBoard b = getBoard();
		int x = getX();
		int y = getY();
		DirectionalTile bottom = b.getTileOrNull(x, y + 1);
		return bottom;
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
	public BottomLeftTile copy() {
		return DirectionalTileBoard.blPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.blPool.free(this);
	}

	@Override
	public BottomRightTile getHorizontalInverted() {
		return DirectionalTileBoard.brPool.obtain();
	}

	@Override
	public TopLeftTile getVerticalInverted() {
		return DirectionalTileBoard.tlPool.obtain();
	}
}
