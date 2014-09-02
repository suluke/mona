package de.lksbhm.mona.puzzle.representations.directional;

public class TopBottomTile extends DirectionalTile {

	TopBottomTile() {
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitTopBottom(this);
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
		DirectionalTile bottom = b.getTileOrNull(x, y + 1);
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
	public TopBottomTile copy() {
		return DirectionalTileBoard.tbPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.tbPool.free(this);
	}

	@Override
	public TopBottomTile getHorizontalInverted() {
		return DirectionalTileBoard.tbPool.obtain();
	}

	@Override
	public TopBottomTile getVerticalInverted() {
		return DirectionalTileBoard.tbPool.obtain();
	}
}
