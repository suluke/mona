package de.lksbhm.mona;

import de.lksbhm.gdx.util.GregorianCalendarInterface;

public class HtmlPlatform extends MonaPlatform {

	@Override
	public GregorianCalendarInterface getToday() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatCalendarLocalized(GregorianCalendarInterface calendar,
			String formatString) {
		return "";
	}

	@Override
	public String formatInt2Digits(int i) {
		return "";
	}

	@Override
	public String getLineSeparator() {
		return "\n";
	}

}
