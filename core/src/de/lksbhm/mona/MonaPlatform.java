package de.lksbhm.mona;

import de.lksbhm.gdx.platforms.AbstractPlatform;
import de.lksbhm.gdx.util.GregorianCalendarInterface;

public abstract class MonaPlatform extends AbstractPlatform {
	/**
	 * The following constraints need to be fulfilled:
	 * {@link GregorianCalendarInterface#getTimeInMillis()} must return the
	 * timestamp of the current day at 0:00 in GMT. The rest is to be
	 * implemented as would be expected.
	 * 
	 * @return
	 */
	public abstract GregorianCalendarInterface getToday();

	public abstract String formatCalendarLocalized(
			GregorianCalendarInterface calendar, String formatString);

	public abstract String formatInt2Digits(int i);

	public abstract String getLineSeparator();
}
