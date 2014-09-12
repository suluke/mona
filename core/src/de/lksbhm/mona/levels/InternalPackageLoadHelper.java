package de.lksbhm.mona.levels;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

class InternalPackageLoadHelper {
	private InternalPackageLoadHelper() {

	}

	private final static String nameSpace = "de.lksbhm.mona";

	public static FileHandle[] getInternalPackageDirs() {
		FileHandle packagesDir = Gdx.files.internal("json/levelpackages");
		if (!packagesDir.isDirectory() || !packagesDir.exists()) {
			// TODO stupid workaround. At least make globally accessible
			packagesDir = Gdx.files
					.internal("../android/assets/json/levelpackages");
		}

		if (!packagesDir.exists()) {
			throw new RuntimeException();
		}
		if (!packagesDir.isDirectory()) {
			throw new RuntimeException();
		}
		FileHandle[] packageDirs = packagesDir.list(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					return false;
				}
				String name = pathname.getName();
				if (name.length() != 2) {
					return false;
				}
				if (Character.isDigit(name.charAt(0))
						&& Character.isDigit(name.charAt(1))) {
					return true;
				}
				return false;
			}
		});
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
		JsonReader reader = new JsonReader();
		return reader.parse(packFile);
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
