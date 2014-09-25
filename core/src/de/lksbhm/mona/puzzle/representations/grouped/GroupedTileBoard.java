package de.lksbhm.mona.puzzle.representations.grouped;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import de.lksbhm.mona.puzzle.representations.Board;

public class GroupedTileBoard extends Board<GroupedTile> implements Disposable {

	static final Pool<GroupedTile> nodePool = new Pool<GroupedTile>() {
		@Override
		protected GroupedTile newObject() {
			return new GroupedTile();
		}
	};

	public GroupedTileBoard(int width, int height) {
		this(width, height, true);
	}

	private GroupedTileBoard(int width, int height, boolean initializeTiles) {
		super(width, height);
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

	@Override
	protected GroupedTile[] createNodeArray(int size) {
		return new GroupedTile[size];
	}
}
