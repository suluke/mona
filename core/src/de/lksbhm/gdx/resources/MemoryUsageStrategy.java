package de.lksbhm.gdx.resources;

public interface MemoryUsageStrategy {
	boolean isReleaseRequired();

	void notifyRelease(ResourceConsumer released);
}
