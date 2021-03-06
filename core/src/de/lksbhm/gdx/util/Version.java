package de.lksbhm.gdx.util;

public class Version {
	public enum Status {
		ALPHA, BETA, RELEASE_CANDIDATE, STABLE
	}

	public enum Visibility {
		INTERNAL, TESTING, PUBLIC
	}

	private final Status status;
	private final Visibility visibility;
	private final int major;
	private final int minor;
	private final int revision;
	private final GregorianCalendarInterface realeaseDate;
	private final String codeName;

	public Version(String codeName, int major, int minor, int revision,
			Status status, Visibility visibility,
			GregorianCalendarInterface releaseDate) {
		this.codeName = codeName;
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.status = status;
		this.visibility = visibility;
		this.realeaseDate = releaseDate;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getRevision() {
		return revision;
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

	public String majorMinorRevision() {
		return major + "." + minor + "." + revision;
	}

	public Visibility getVisibility() {
		return visibility;
	}
}
