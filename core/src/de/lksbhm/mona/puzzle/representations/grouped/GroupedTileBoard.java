package de.lksbhm.mona.puzzle.representations.grouped;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

import de.lksbhm.mona.puzzle.representations.Board;

public class GroupedTileBoard extends Board<GroupedTile> implements Disposable {

	static final Pool<GroupedTile> nodePool = new ReflectionPool<GroupedTile>(
			GroupedTile.class);

	public GroupedTileBoard(int width, int height) {
		this(width, height, true);
	}

	private GroupedTileBoard(int width, int height, boolean initializeTiles) {
		super(width, height, GroupedTile.class);
		if (initializeTiles) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					setTile(nodePool.obtain(), x, y);
				}
			}
		}
	}

	@Override
	public void dispose() {
		for (GroupedTile tile : this) {
			nodePool.free(tile);
		}
	}

	@Override
	protected Board<GroupedTile> instantiate(int width, int height) {
		return new GroupedTileBoard(width, height, false);
	}
}
