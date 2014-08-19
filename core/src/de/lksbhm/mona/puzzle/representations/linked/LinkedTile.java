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

}
