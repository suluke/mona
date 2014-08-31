package de.lksbhm.gdx.resources;

public class NeverReleaseStrategy implements MemoryUsageStrategy {

	@Override
	public boolean isReleaseRequired() {
		return false;
	}

	@Override
	public void notifyRelease(ResourceConsumer consumer) {
	}

}
