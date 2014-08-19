package de.lksbhm.mona.puzzle.representations.grouped;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

import de.lksbhm.mona.puzzle.representations.Board;

public class GroupedTileBoard extends Board<GroupedTile> implements Disposable {

	private static final Pool<GroupedTile> nodePool = new ReflectionPool<GroupedTile>(
			GroupedTile.class);

	public GroupedTileBoard(int width, int height) {
		super(width, height, GroupedTile.class);
		GroupedTile[][] tiles = getTiles();
		for (GroupedTile[] array : tiles) {
			for (int y = 0; y < height; y++) {
				array[y] = nodePool.obtain();
			}
		}
	}

	@Override
	public void dispose() {
		GroupedTile[][] tiles = getTiles();
		for (GroupedTile[] array : tiles) {
			for (GroupedTile tile : array) {
				nodePool.free(tile);
			}
		}
	}
}
