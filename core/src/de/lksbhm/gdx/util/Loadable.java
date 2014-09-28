package de.lksbhm.gdx.util;

public interface Loadable<Loaded> {
	/**
	 * 
	 * @return true if finished loading
	 */
	boolean update();

	/**
	 * 
	 * @return between 0 and 1
	 */
	float getProgress();

	/**
	 * Blocks until loaded
	 */
	void finish();

	/**
	 * 
	 * @return the loaded resource
	 * @throws IllegalStateException
	 *             if not yet loaded (see {@link #update()})
	 */
	Loaded get();
}
