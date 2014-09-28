package de.lksbhm.mona.ui.actors;

public interface PuzzleModificationListener {
	void onAddConnection();

	void onClearTileConnections();

	void onClearAllTileConnections();
}
