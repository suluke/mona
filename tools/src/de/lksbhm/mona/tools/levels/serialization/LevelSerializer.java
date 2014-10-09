package de.lksbhm.mona.tools.levels.serialization;

import de.lksbhm.mona.levels.DailyLevel;
import de.lksbhm.mona.levels.GeneratedLevel;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.tools.levels.LevelVisitor;

public class LevelSerializer {
	private LevelSerializer() {
	}

	public static String toJson(Level level) {
		LevelJSONSerializationDispatcher dispatcher = new LevelJSONSerializationDispatcher();
		LevelVisitor.dispatch(level, dispatcher);
		return dispatcher.getSerialization();
	}

	private static class LevelJSONSerializationDispatcher extends LevelVisitor {
		private String serializedJson;

		@Override
		public void visitLiterallyDefinedLevel(LiterallyDefinedLevel level) {
			serializedJson = new LiterallyDefinedLevelSerializer(level)
					.getSerialization();
		}

		@Override
		public void visitGeneratedLevel(GeneratedLevel level) {
			serializedJson = new GeneratedLevelSerializer(level)
					.getSerialization();
		}

		@Override
		public void visitDailyLevel(DailyLevel level) {
			throw new UnsupportedOperationException(
					"DailyLevels cannot be serialized");
		}

		public String getSerialization() {
			return serializedJson;
		}
	};
}
