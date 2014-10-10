package de.lksbhm.mona.tools.levels.serialization;

import java.io.IOException;
import java.io.StringWriter;

import de.lksbhm.gdx.util.JsonWriter;
import de.lksbhm.mona.levels.LevelLoadHelper;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.Puzzle;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTile;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

public class LiterallyDefinedLevelSerializer {

	private final StringWriter stringWriter = new StringWriter();
	private final JsonWriter jsonWriter = new JsonWriter(stringWriter);
	private final LiterallyDefinedLevel level;
	private final boolean useNamespace;

	public LiterallyDefinedLevelSerializer(LiterallyDefinedLevel level,
			boolean namespace) {
		this.level = level;
		useNamespace = namespace;
		jsonWriter.setIndentation(true);

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
			Puzzle puzzle = level.getPuzzle();
			for (int x = 0; x < puzzle.getWidth(); x++) {
				jsonWriter.array();
				for (int y = 0; y < puzzle.getHeight(); y++) {
					String type = serializeType(puzzle.getTile(x, y).getType());
					jsonWriter.value(type);
				}
				jsonWriter.pop();
			}
			jsonWriter.pop();
			jsonWriter.array("solution");
			DirectionalTileBoard solution = level.getSolution();
			for (int x = 0; x < solution.getWidth(); x++) {
				jsonWriter.array();
				for (int y = 0; y < solution.getHeight(); y++) {
					String type = serializeDirectionalTile(solution.getTile(x,
							y));
					jsonWriter.value(type);
				}
				jsonWriter.pop();
			}
			jsonWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String serializeType(Type type) {
		return type.toString();
	}

	private static String serializeDirectionalTile(DirectionalTile tile) {
		String name = tile.getClass().getSimpleName();
		return name.substring(0, name.length() - "Tile".length());
	}

	public String getSerialization() {
		return stringWriter.toString();
	}

}
