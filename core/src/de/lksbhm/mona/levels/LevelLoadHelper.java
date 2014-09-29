package de.lksbhm.mona.levels;

import java.util.Random;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.lksbhm.mona.puzzle.Piece.Type;
import de.lksbhm.mona.puzzle.representations.directional.DirectionalTileBoard;

class LevelLoadHelper {
	private final static String nameSpace = "de.lksbhm.mona";

	private LevelLoadHelper() {

	}

	public static Level fromJsonForPackage(FileHandle file, LevelPackage pack) {
		return fromJsonForPackage(file.readString(), pack);
	}

	public static Level fromJsonForPackage(String json, LevelPackage pack) {
		JsonValue val = new JsonReader().parse(json);
		return fromJsonForPackage(val, pack);
	}

	public static Level fromJsonForPackage(JsonValue json, LevelPackage pack) {
		json = stripContainer(json);
		String typeName = json.getString("type");
		Level level;
		if ("LiterallyDefined".equals(typeName)) {
			level = getLiterallyDefinedLevelFromJson(json, pack);
		} else if ("Generated".equals(typeName)) {
			level = getGeneratedLevelFromJson(json, pack);
		} else {
			throw new RuntimeException();
		}
		if (json.has("tutorial")) {
			String tutorialName = json.getString("tutorial");
			level.setTutorialByName(tutorialName);
		}
		return level;
	}

	private static Level getGeneratedLevelFromJson(JsonValue json,
			LevelPackage pack) {
		String id = json.getString("id");
		JsonValue data = json.get("data");
		long seed = data.getLong("seed");
		Random r;
		if (data.has("generator")) {
			String generatorName = data.getString("generator");
			Class<?> classWithName;
			try {
				classWithName = ClassReflection.forName(generatorName);
			} catch (ReflectionException e) {
				throw new RuntimeException(e);
			}
			if (ClassReflection.isAssignableFrom(Random.class, classWithName)) {
				@SuppressWarnings("unchecked")
				Class<? extends Random> generatorClass = (Class<? extends Random>) classWithName;
				try {
					Constructor ctor = ClassReflection.getConstructor(
							generatorClass, new Class<?>[0]);
					ctor.setAccessible(true);
					r = (Random) ctor.newInstance(new Object[0]);
				} catch (ReflectionException e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException();
			}
		} else {
			r = new Random();
		}
		int[][] invisibleTiles = null;
		if (data.has("invisibleTiles")) {
			JsonValue invisibleTilesJson = data.get("invisibleTiles");
			invisibleTiles = new int[invisibleTilesJson.size][2];
			int i = 0;
			for (JsonValue coord : invisibleTilesJson) {
				invisibleTiles[i][0] = coord.getInt(0);
				invisibleTiles[i][1] = coord.getInt(1);
				i++;
			}
		}
		r.setSeed(seed);
		return new GeneratedLevel(seed, r, pack, id, invisibleTiles);
	}

	private static Level getLiterallyDefinedLevelFromJson(JsonValue json,
			LevelPackage pack) {
		String id = json.getString("id");
		JsonValue data = json.get("data");
		JsonValue tileTypesArray = data.get("tileTypes");
		JsonValue solutionArray = data.get("solution");
		int width = tileTypesArray.size;
		int height = tileTypesArray.get(0).size;

		Type[][] tileTypes = new Type[width][height];
		DirectionalTileBoard solution = new DirectionalTileBoard(width, height);

		JsonValue currentTileTypesColumn;
		JsonValue currentSolutionColumn;
		for (int x = 0; x < width; x++) {
			currentTileTypesColumn = tileTypesArray.get(x);
			currentSolutionColumn = solutionArray.get(x);
			for (int y = 0; y < height; y++) {
				tileTypes[x][y] = Type.fromString(currentTileTypesColumn
						.getString(y));
				setSolutionTileByString(currentSolutionColumn.getString(y),
						solution, x, y);
			}
		}

		LiterallyDefinedLevel level = new LiterallyDefinedLevel(tileTypes,
				solution, pack, id);
		return level;
	}

	private static void setSolutionTileByString(String string,
			DirectionalTileBoard solution, int x, int y) {
		if ("TopBottom".equalsIgnoreCase(string)) {
			solution.setTileToTopBottom(x, y);
		} else if ("TopLeft".equalsIgnoreCase(string)) {
			solution.setTileToTopLeft(x, y);
		} else if ("TopRight".equalsIgnoreCase(string)) {
			solution.setTileToTopRight(x, y);
		} else if ("BottomLeft".equalsIgnoreCase(string)) {
			solution.setTileToBottomLeft(x, y);
		} else if ("BottomRight".equalsIgnoreCase(string)) {
			solution.setTileToBottomRight(x, y);
		} else if ("LeftRight".equalsIgnoreCase(string)) {
			solution.setTileToLeftRight(x, y);
		}
		if ("NoDirection".equalsIgnoreCase(string)) {
			solution.setTileToNoDirection(x, y);
		}
	}

	private static JsonValue stripContainer(JsonValue json) {
		if (json.has(nameSpace)) {
			json = json.get(nameSpace).get("levels").get(0);
		}
		return json;
	}
}
