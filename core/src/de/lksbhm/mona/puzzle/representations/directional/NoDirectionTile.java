package de.lksbhm.mona.puzzle.representations.directional;

public class NoDirectionTile extends DirectionalTile {

	NoDirectionTile() {
	}

	@Override
	public void acceptVisitor(DirectionalTileVisitor visitor) {
		visitor.visitEmpty(this);
	}

	@Override
	public DirectionalTile getFirstAdjacent() {
		return null;
	}

	@Override
	public DirectionalTile getSecondAdjacent() {
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

	@Override
	public NoDirectionTile copy() {
		return DirectionalTileBoard.emptyPool.obtain();
	}

	@Override
	public void dispose() {
		DirectionalTileBoard.emptyPool.free(this);
	}

	@Override
	public NoDirectionTile getHorizontalInverted() {
		return copy();
	}

	@Override
	public NoDirectionTile getVerticalInverted() {
		return copy();
	}
}
