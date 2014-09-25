package de.lksbhm.mona;

import de.lksbhm.gdx.platforms.AbstractPlatform;
import de.lksbhm.gdx.util.GregorianCalendarInterface;

public abstract class MonaPlatform extends AbstractPlatform {
	/**
	 * 
	 * How to implement: Retrieve the localized year, month and day at the time
	 * this method is called. Then interpret those as a date in GMT timezone and
	 * retrieve the milliseconds of that date at 0:00am. Build a
	 * {@link GregorianCalendarInterface} from the information you just received
	 * (day, month, year, millis) and set its timezone to "GMT"
	 * 
	 * @return
	 */
	public abstract GregorianCalendarInterface getToday();

	public abstract String formatCalendarLocalized(
			GregorianCalendarInterface calendar, String formatString);

	public abstract String formatInt2Digits(int i);

	public abstract String getLineSeparator();
}
