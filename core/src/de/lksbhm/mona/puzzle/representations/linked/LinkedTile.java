package de.lksbhm.mona.puzzle.representations.linked;

import de.lksbhm.mona.puzzle.representations.Tile;

public class LinkedTile extends Tile<LinkedTile> {
	public LinkedTile() {
		super(LinkedTile.class);
	}

	private LinkedTile parent = null;
	private LinkedTile child = null;

	public LinkedTile getParent() {
		return parent;
	}

	public void setParent(LinkedTile parent) {
		this.parent = parent;
	}

	/**
	 * @return the child
	 */
	public LinkedTile getChild() {
		return child;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setChild(LinkedTile child) {
		this.child = child;
	}

	@Override
	public LinkedTile copy() {
		LinkedTile t = LinkedTileBoard.directedNodePool.obtain();
		// TODO cannot imagine better way to do this
		t.child = child;
		t.parent = parent;
		return t;
	}

	@Override
	public void dispose() {
		LinkedTileBoard.directedNodePool.free(this);
	}
}
