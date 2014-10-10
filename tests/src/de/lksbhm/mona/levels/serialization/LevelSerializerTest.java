package de.lksbhm.mona.levels.serialization;

import junit.framework.TestCase;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelLoadHelper;
import de.lksbhm.mona.levels.LevelMethodAccessHelper;
import de.lksbhm.mona.puzzle.PuzzleMethodAccessHelper;
import de.lksbhm.mona.tools.levels.serialization.LevelSerializer;
import de.lksbhm.test.util.FileUtils;

public class LevelSerializerTest extends TestCase {
	public void testLiteralToJson() {
		// load test level
		String json = FileUtils.readFile("json/levelpackages/00/00.json");
		JsonReader reader = new JsonReader();
		JsonValue val = reader.parse(json);
		// remove tutorial because it expects gui
		val.child().child().child().remove("tutorial");
		Level level = LevelLoadHelper.fromJsonForPackage(val, null);

		// serialize level and import it again
		json = LevelSerializer.toJson(level, true);
		val = reader.parse(json);
		Level level2 = LevelLoadHelper.fromJsonForPackage(val, null);

		// apply the solution of the old one and assert that the new one is
		// solved by it
		LevelMethodAccessHelper.setSolution(level2, level.getSolution());
		level2.applySolution();
		// for a call to updateSolvedState in puzzle2:
		assertTrue(PuzzleMethodAccessHelper.isSolved(level2.getPuzzle()));
	}
}
