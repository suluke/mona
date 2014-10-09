package de.lksbhm.mona.levels.serialization;

import junit.framework.TestCase;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelLoadHelper;
import de.lksbhm.mona.tools.levels.serialization.LevelSerializer;
import de.lksbhm.test.util.FileUtils;

public class LevelSerializerTest extends TestCase {
	public void testLiteralToJson() {
		String json = FileUtils.readFile("json/levelpackages/00/00.json");
		JsonReader reader = new JsonReader();
		JsonValue val = reader.parse(json);
		val.child().child().child().remove("tutorial");
		Level level = LevelLoadHelper.fromJsonForPackage(val, null);
		System.out.println(LevelSerializer.toJson(level, true));
	}
}
