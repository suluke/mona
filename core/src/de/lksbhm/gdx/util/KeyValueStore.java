package de.lksbhm.gdx.util;

public interface KeyValueStore<Context> {
	public String get(String key, Context context);

	public void put(String key, String value, Context context);
}
