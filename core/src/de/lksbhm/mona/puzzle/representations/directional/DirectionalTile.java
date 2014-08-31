package de.lksbhm.mona.puzzle.representations.directional;

import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.puzzle.representations.Tile;

public abstract class DirectionalTile extends Tile<DirectionalTile> {
	public DirectionalTile() {
		super(DirectionalTile.class);
	}

	public abstract void acceptVisitor(DirectionalTileVisitor visitor);

	public Pair<DirectionalTile, DirectionalTile> getAdjacent() {
		return new Pair<DirectionalTile, DirectionalTile>(getFirstAdjacent(),
				getSecondAdjacent());
	}

	public abstract DirectionalTile getFirstAdjacent();

	public abstract DirectionalTile getSecondAdjacent();

	public abstract boolean isEmpty();

	public abstract boolean isStraight();

	public abstract DirectionalTile getHorizontalInverted();

	public abstract DirectionalTile getVerticalInverted();

	@Override
	protected DirectionalTileBoard getBoard() {
		return (DirectionalTileBoard) super.getBoard();
	}
}
