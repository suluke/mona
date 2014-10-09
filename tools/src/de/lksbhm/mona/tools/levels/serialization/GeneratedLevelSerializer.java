package de.lksbhm.mona.tools.levels.serialization;

import de.lksbhm.mona.levels.GeneratedLevel;

class GeneratedLevelSerializer {
	private final StringBuilder sb = new StringBuilder();
	private final GeneratedLevel level;

	GeneratedLevelSerializer(GeneratedLevel level) {
		this.level = level;
	}

	public String getSerialization() {
		return sb.toString();
	}
}
