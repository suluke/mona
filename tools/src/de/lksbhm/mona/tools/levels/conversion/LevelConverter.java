package de.lksbhm.mona.tools.levels.conversion;

import de.lksbhm.mona.levels.DailyLevel;
import de.lksbhm.mona.levels.GeneratedLevel;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;
import de.lksbhm.mona.tools.levels.LevelVisitor;

public class LevelConverter {
	private LevelConverter() {

	}

	public static LiterallyDefinedLevel toLiteral(Level level) {
		ToLiteralLevelDispatcher dispatcher = new ToLiteralLevelDispatcher();
		LevelVisitor.dispatch(level, dispatcher);
		return dispatcher.getConvertedLevel();
	}

	private static class ToLiteralLevelDispatcher extends LevelVisitor {
		private LiterallyDefinedLevel converted;

		@Override
		public void visitLiterallyDefinedLevel(LiterallyDefinedLevel level) {
			converted = level;
		}

		@Override
		public void visitGeneratedLevel(GeneratedLevel level) {
			converted = new GeneratedToLiteral(level).getConverted();

		}

		@Override
		public void visitDailyLevel(DailyLevel level) {
			converted = new DailyToLiteral(level).getConverted();
		}

		public LiterallyDefinedLevel getConvertedLevel() {
			return converted;
		}
	}
}
