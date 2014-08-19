package de.lksbhm.gdx.resources;

public interface MemoryUsageStrategy {
	boolean isReleaseRequired();

	void notifyReleaseEstimate(long estimate);
}
