package de.lksbhm.gdx;

public abstract class Settings {
	public String getUserSettingsPrefix() {
		return "user";
	}

	public boolean isAllowEqualUserNames() {
		return false;
	}
}
