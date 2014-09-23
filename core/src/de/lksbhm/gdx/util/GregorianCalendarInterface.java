package de.lksbhm.gdx.util;


public interface GregorianCalendarInterface {
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	public static final int SATURDAY = 7;
	public static final int SUNDAY = 1;

	long getTimeInMillis();

	int getDayOfWeek();

	int getDayOfMonth();

	int getMonth();

	int getYear();

	String getTimeZone();
}
