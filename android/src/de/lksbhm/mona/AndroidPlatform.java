package de.lksbhm.mona;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.gdx.util.GregorianCalendarValue;

public class AndroidPlatform extends MonaPlatform {
	private GregorianCalendarInterface today;
	private final Calendar cal = Calendar.getInstance();

	@Override
	public GregorianCalendarInterface getToday() {
		if (today == null) {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH);
			int day = now.get(Calendar.DAY_OF_MONTH);
			int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
			TimeZone gmt = TimeZone.getTimeZone("GMT");
			now.setTimeZone(gmt);
			now.setTimeInMillis(0);
			now.set(year, month, day);
			long millis = now.getTimeInMillis();
			GregorianCalendarValue today = new GregorianCalendarValue();
			today.setYear(year);
			today.setMonth(month);
			today.setDayOfMonth(day);
			today.setDayOfWeek(dayOfWeek);
			today.setTimeInMillis(millis);
			today.setTimeZone("GMT");
			this.today = today;
		}
		return today;
	}

	@Override
	public String formatCalendarLocalized(GregorianCalendarInterface calendar,
			String formatString) {
		cal.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone()));
		cal.setTimeInMillis(calendar.getTimeInMillis());
		return new SimpleDateFormat(formatString).format(new Date(cal
				.getTimeInMillis()));
	}

	@Override
	public String formatInt2Digits(int i) {
		return String.format("%1$02d", i);
	}

	@Override
	public String getLineSeparator() {
		return "\n";
	}
}
