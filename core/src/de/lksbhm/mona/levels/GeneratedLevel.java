package de.lksbhm.mona.levels;

import de.lksbhm.mona.puzzle.Puzzle;

public class GeneratedLevel extends Level {

	private final Puzzle p;
	private String levelId = "generated";
	private String packageId = "generated";

	public GeneratedLevel(Puzzle p) {
		this.p = p;
	}

	@Override
	public Puzzle getPuzzle() {
		return p;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Override
	public String getPackageId() {
		return packageId;
	}

	@Override
	public String getLevelId() {
		return levelId;
	}

}
