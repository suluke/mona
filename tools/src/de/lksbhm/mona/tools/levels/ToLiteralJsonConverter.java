package de.lksbhm.mona.tools.levels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelLoadHelper;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.mock.Mock;
import de.lksbhm.mona.tools.levels.conversion.LevelConverter;
import de.lksbhm.mona.tools.levels.serialization.LevelSerializer;
import de.lksbhm.util.FileUtils;

public class ToLiteralJsonConverter {
	public static void main(String[] args) throws IOException {
		Mock.simpleMock();

		String pack = args[0];
		String levelName = args[1];
		String levelString;
		if (pack != null && levelName != null) {
			levelString = FileUtils.readFile("json/levelpackages/" + pack + "/"
					+ levelName + ".json");
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String s;
			StringBuilder sb = new StringBuilder();
			while ((s = in.readLine()) != null && s.length() != 0) {
				sb.append(s);
			}
			levelString = sb.toString();
		}
		Level level = LevelLoadHelper.fromJsonForPackage(levelString, null);
		LiterallyDefinedLevel literalLevel = LevelConverter.toLiteral(level);
		System.out.println(LevelSerializer.toJson(literalLevel, true));
	}
}
