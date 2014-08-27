package de.lksbhm.mona.puzzle.representations.undirected;

import de.lksbhm.gdx.util.Pair;
import de.lksbhm.mona.puzzle.representations.Tile;

public abstract class UndirectedTile extends Tile<UndirectedTile> {
	public UndirectedTile() {
		super(UndirectedTile.class);
	}

	public abstract void acceptVisitor(UndirectedTileVisitor visitor);

	public Pair<UndirectedTile, UndirectedTile> getAdjacent() {
		return new Pair<UndirectedTile, UndirectedTile>(getFirstAdjacent(),
				getSecondAdjacent());
	}

	public abstract UndirectedTile getFirstAdjacent();

	public abstract UndirectedTile getSecondAdjacent();

	public abstract boolean isEmpty();

	public abstract boolean isStraight();

	public abstract UndirectedTile getHorizontalInverted();

	public abstract UndirectedTile getVerticalInverted();

	@Override
	protected UndirectedTileBoard getBoard() {
		return (UndirectedTileBoard) super.getBoard();
	}
}
