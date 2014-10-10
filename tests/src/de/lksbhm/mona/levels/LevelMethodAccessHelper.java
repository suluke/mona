package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LevelMethodAccessHelper {
	@SuppressWarnings("deprecation")
	public static void setSolution(Level level, DirectionalTileBoard solution) {
		level.setSolution(solution);
	}
}
