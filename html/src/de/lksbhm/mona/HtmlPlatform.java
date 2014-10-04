package de.lksbhm.mona;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import de.lksbhm.gdx.util.GregorianCalendarInterface;
import de.lksbhm.gdx.util.GregorianCalendarValue;

public class HtmlPlatform extends MonaPlatform {

	private static final NumberFormat twoDigitnumberFormatter = NumberFormat
			.getFormat("00");

	@Override
	public GregorianCalendarInterface getToday() {
		GregorianCalendarValue cal = new GregorianCalendarValue();
		Date today = new Date();
		int year = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(
				today));
		int month = Integer.parseInt(DateTimeFormat.getFormat("M")
				.format(today));
		int day = Integer.parseInt(DateTimeFormat.getFormat("d").format(today));
		int dayOfWeek = Integer.parseInt(DateTimeFormat.getFormat("c").format(
				today));
		// Seems like sunday is 0 in GWT
		dayOfWeek++;
		cal.setYear(year);
		cal.setMonth(month);
		cal.setDayOfMonth(day);
		cal.setDayOfWeek(dayOfWeek);
		cal.setTimeZone("GMT");
		DateTimeFormat.getFormat("ZZZZ yyyy/M/d").parse(
				"GMT:+0000 " + year + "/" + month + "/" + day, 0, today);
		cal.setTimeInMillis(today.getTime());
		return cal;
	}

	@Override
	public String formatCalendarLocalized(GregorianCalendarInterface calendar,
			String formatString) {
		return DateTimeFormat.getFormat(formatString).format(
				new Date(calendar.getTimeInMillis()));
	}

	@Override
	public String formatInt2Digits(int i) {
		return twoDigitnumberFormatter.format(i);
	}

	@Override
	public String getLineSeparator() {
		return "\n";
	}

}
