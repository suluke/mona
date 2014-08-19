package de.lksbhm.mona;

/**
 * Platform dependent providers can be accessed using this class.
 * 
 * @author suluke
 *
 */
public class Platform {
	private Platform() {

	}

	private static PlatformInterface instance;

	static interface PlatformInterface {

	}

	static void setPlatform(PlatformInterface platform) {
		instance = platform;
	}

	public static PlatformInterface get() {
		return instance;
	}
}
