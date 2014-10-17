package de.lksbhm.mona.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import de.lksbhm.mona.Mona;
import de.lksbhm.mona.MonaPlatform;

class InternalPackageLoadHelper {
	private InternalPackageLoadHelper() {

	}

	private final static String packagesDirPath = "json/levelpackages";
	private final static String nameSpace = "de.lksbhm.mona";
	private final static JsonReader jsonReader = new JsonReader();

	public static FileHandle[] getInternalPackageDirs() {
		MonaPlatform platform = Mona.getGame().getPlatformManager()
				.getPlatform();

		FileHandle packagesDir = Gdx.files.internal(packagesDirPath);
		FileHandle packagesJsonFile = packagesDir.child("packages.json");
		JsonValue packagesJson = jsonReader.parse(packagesJsonFile);
		int numberOfPackages = packagesJson.get(nameSpace).getInt(
				"numberOfInternalPackages");
		FileHandle[] packageDirs = new FileHandle[numberOfPackages];
		String packageDirName;
		for (int i = 0; i < packageDirs.length; i++) {
			packageDirName = platform.formatInt2Digits(i);
			packageDirs[i] = packagesDir.child(packageDirName);
			// TODO fileHandle verification
		}
		return packageDirs;
	}

	public static JsonValue[] getInternalPackageJsons(
			FileHandle[] internalPackageDirs) {
		JsonValue[] result = new JsonValue[internalPackageDirs.length];
		FileHandle packFile;
		for (int i = 0; i < result.length; i++) {
			packFile = internalPackageDirs[i].child("package.json");
			result[i] = readPackageJson(packFile);
		}
		return result;
	}

	public static JsonValue readPackageJson(FileHandle packFile) {
		return jsonReader.parse(packFile);
	}

	public static JsonValue[] removePackageJsonContainers(JsonValue[] jsons) {
		JsonValue currentJson;
		for (int i = 0; i < jsons.length; i++) {
			currentJson = jsons[i];
			if (currentJson.has(nameSpace)) {
				jsons[i] = currentJson.get(nameSpace).get("packages").get(0);
			}
		}
		return jsons;
	}

	public static LevelPackageCollection getInternalPackages() {
		FileHandle[] packagesDirs = getInternalPackageDirs();
		JsonValue[] packageJsons = getInternalPackageJsons(packagesDirs);
		packageJsons = removePackageJsonContainers(packageJsons);
		LevelPackageCollection collection = new LevelPackageCollection(
				packagesDirs.length);
		InternalPackage pack;
		String id;
		int size;
		Difficulty d;
		JsonValue currentValue;
		for (int i = 0; i < packagesDirs.length; i++) {
			currentValue = packageJsons[i];
			id = currentValue.getString("id");
			d = Difficulty.fromString(currentValue.getString("difficulty"));
			size = currentValue.getInt("size");
			pack = new InternalPackage(id, size, d, collection, packagesDirs[i]);
			collection.setPackage(i, pack);
		}
		return collection;
	}
}
