package de.lksbhm.gdx.util;


public class GregorianCalendarValue implements GregorianCalendarInterface {

	private int year;
	private int month;
	private int dayOfMonth;
	private int dayOfWeek;
	private long timeInMillis;
	private String timezone;

	@Override
	public long getTimeInMillis() {
		return timeInMillis;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @param dayOfMonth
	 *            the dayOfMonth to set
	 */
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * @param dayOfWeek
	 *            the dayOfWeek to set
	 */
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * @param timeInMillis
	 *            the timeInMillis to set
	 */
	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}

	@Override
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	@Override
	public int getDayOfMonth() {
		return dayOfMonth;
	}

	@Override
	public int getMonth() {
		return month;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public String getTimeZone() {
		return timezone;
	}

	public void setTimeZone(String ts) {
		this.timezone = ts;
	}

}
