package de.lksbhm.gdx.util;

public interface KeyValueStore<Context> {
	public float getFloat(String key, Context context);

	public double getDouble(String key, Context context);

	public int getInt(String key, Context context);

	public long getLong(String key, Context context);

	public boolean getBoolean(String key, Context context);

	public String get(String key, Context context);

	public void put(String key, double value, Context context);

	public void put(String key, float value, Context context);

	public void put(String key, long value, Context context);

	public void put(String key, int value, Context context);

	public void put(String key, boolean value, Context context);

	public void put(String key, String value, Context context);
}
