package de.lksbhm.gdx.platforms;

public class PlatformManager<Platform extends AbstractPlatform> {
	private Platform platform;

	void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Platform getPlatform() {
		return platform;
	}
}
