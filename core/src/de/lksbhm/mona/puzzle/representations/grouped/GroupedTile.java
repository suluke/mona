package de.lksbhm.mona.puzzle.representations.grouped;

import de.lksbhm.mona.puzzle.representations.Board;
import de.lksbhm.mona.puzzle.representations.Tile;

public class GroupedTile extends Tile<GroupedTile> {

	public GroupedTile() {
		super(GroupedTile.class);
	}

	private TileGroupType type;

	/**
	 * @return the type
	 */
	public TileGroupType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TileGroupType type) {
		this.type = type;
	}

	@Override
	@Deprecated
	public void setup(Board<GroupedTile> b, int x, int y) {
		super.setup(b, x, y);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setup(Board<GroupedTile> b, int x, int y, TileGroupType type) {
		super.setup(b, x, y);
		this.type = type;
	}

	@Override
	public GroupedTile copy() {
		GroupedTile t = GroupedTileBoard.nodePool.obtain();
		t.type = type;
		return t;
	}

	@Override
	public void dispose() {
		GroupedTileBoard.nodePool.free(this);
	}
}
