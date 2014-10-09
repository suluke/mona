package de.lksbhm.mona.tools.levels.serialization;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.utils.JsonWriter;

import de.lksbhm.mona.levels.LevelLoadHelper;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;

public class LiterallyDefinedLevelSerializer {

	private final StringWriter stringWriter = new StringWriter();
	private final JsonWriter jsonWriter = new JsonWriter(stringWriter);
	private final LiterallyDefinedLevel level;
	private final boolean useNamespace;

	public LiterallyDefinedLevelSerializer(LiterallyDefinedLevel level,
			boolean namespace) {
		this.level = level;
		useNamespace = namespace;

		serializeLevel();
	}

	private void serializeLevel() {
		try {
			jsonWriter.object();
			if (useNamespace) {
				jsonWriter.object(LevelLoadHelper.nameSpace);
			}
			jsonWriter.array("levels");
			jsonWriter.object();
			jsonWriter.set("id", level.getLevelId());
			jsonWriter.set("type", "LiterallyDefined");
			if (level.hasTutorial()) {
				jsonWriter.set("tutorial", level.getTutorialName());
			}
			jsonWriter.object("data");
			jsonWriter.array("tileTypes");
			jsonWriter.pop();
			jsonWriter.array("solution");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getSerialization() {
		return stringWriter.toString();
	}

}
