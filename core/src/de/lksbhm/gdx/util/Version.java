package de.lksbhm.gdx.util;

public class Version {
	public enum Status {
		ALPHA, BETA, RELEASE_CANDIDATE, STABLE
	}

	private final Status status;
	private final int major;
	private final int minor;
	private final int revision;
	private final GregorianCalendarInterface realeaseDate;
	private final String codeName;

	public Version(String codeName, int major, int minor, int revision,
			Status status, GregorianCalendarInterface releaseDate) {
		this.codeName = codeName;
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.status = status;
		this.realeaseDate = releaseDate;
	}

	public String getCodeName() {
		return codeName;
	}

	public Status getStatus() {
		return status;
	}

	public GregorianCalendarInterface getReleaseDate() {
		return realeaseDate;
	}
}
