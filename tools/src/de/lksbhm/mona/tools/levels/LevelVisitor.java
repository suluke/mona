package de.lksbhm.mona.tools.levels;

import de.lksbhm.mona.levels.DailyLevel;
import de.lksbhm.mona.levels.GeneratedLevel;
import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LiterallyDefinedLevel;

public abstract class LevelVisitor {
	public abstract void visitLiterallyDefinedLevel(LiterallyDefinedLevel level);

	public abstract void visitGeneratedLevel(GeneratedLevel level);

	public abstract void visitDailyLevel(DailyLevel level);

	public static void dispatch(Level level, LevelVisitor visitor) {
		Class<? extends Level> clazz = level.getClass();
		if (clazz == LiterallyDefinedLevel.class) {
			visitor.visitLiterallyDefinedLevel((LiterallyDefinedLevel) level);
		} else if (clazz == GeneratedLevel.class) {
			visitor.visitGeneratedLevel((GeneratedLevel) level);
		} else if (clazz == DailyLevel.class) {
			visitor.visitDailyLevel((DailyLevel) level);
		} else {
			throw new RuntimeException("Could not dispatch level with class"
					+ clazz.getSimpleName());
		}
	}
}
