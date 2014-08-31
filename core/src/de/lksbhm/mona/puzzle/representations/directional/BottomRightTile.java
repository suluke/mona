package de.lksbhm.mona.puzzle.representations.directional;

public class BottomRightTile extends DirectionalTile {

	public BottomRightTile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitBottomRight(this);
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
	public BottomRightTile copy() {
		return DirectionalTileBoard.brPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.brPool.free(this);
	}

	@Override
	public BottomLeftTile getHorizontalInverted() {
		return DirectionalTileBoard.blPool.obtain();
	}

	@Override
	public TopRightTile getVerticalInverted() {
		return DirectionalTileBoard.trPool.obtain();
	}
}
