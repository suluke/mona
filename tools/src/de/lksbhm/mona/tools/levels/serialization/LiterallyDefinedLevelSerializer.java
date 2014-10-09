package de.lksbhm.mona.tools.levels.serialization;

import de.lksbhm.mona.levels.LiterallyDefinedLevel;

public class LiterallyDefinedLevelSerializer {

	private final StringBuilder sb = new StringBuilder();
	private final LiterallyDefinedLevel level;

	public LiterallyDefinedLevelSerializer(LiterallyDefinedLevel level) {
		this.level = level;
	}

	public String getSerialization() {
		return sb.toString();
	}

}
