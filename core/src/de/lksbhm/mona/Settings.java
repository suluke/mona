package de.lksbhm.mona;

import java.util.ArrayList;

import de.lksbhm.mona.levels.Level;
import de.lksbhm.mona.levels.LevelPackage;

public class Settings extends de.lksbhm.gdx.Settings {
	public StaticSettings statics = new StaticSettings();

	public String getCurrentUserName() {
		return "default-user";
	}

	public String getEscapedCurrentUserName() {
		return getCurrentUserName();
	}

	public String[] getSolvedLevelIdsForPackage(LevelPackage pack) {
		ArrayList<String> list = new ArrayList<String>();
		String key;
		for (Level level : pack) {
			key = pack.getPackageId() + '/' + level.getLevelId() + ".isSolved";
			if (getBoolean(key, getEscapedCurrentUserName())) {
				list.add(level.getLevelId());
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public void putSolveldLevelIdForPackage(String id, LevelPackage pack) {
		String key;
		for (Level level : pack) {
			key = pack.getPackageId() + '/' + level.getLevelId() + ".isSolved";
			if (level.isSolved()) {
				putBoolean(key, true, getEscapedCurrentUserName());
			} else {
				putBoolean(key, false, getEscapedCurrentUserName());
			}
		}
	}
}
