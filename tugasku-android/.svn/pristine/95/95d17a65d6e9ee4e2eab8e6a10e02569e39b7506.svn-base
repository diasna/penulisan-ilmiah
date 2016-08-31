package id.ac.gunadarma.tugasku.helper;

import java.util.Calendar;

public class Util {
	public static int countDays(Calendar to) {
		Calendar from = Calendar.getInstance();
		return diff(to.get(Calendar.YEAR), (to.get(Calendar.MONTH)+1), to.get(Calendar.DAY_OF_MONTH), 
				from.get(Calendar.YEAR), (from.get(Calendar.MONTH)+1), from.get(Calendar.DAY_OF_MONTH));
	}

	public static int julianDay(int year, int month, int day) {
		int a = (14 - month) / 12;
		int y = year + 4800 - a;
		int m = month + 12 * a - 3;
		int jdn = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400
				- 32045;
		return jdn;
	}

	public static int diff(int y1, int m1, int d1, int y2, int m2, int d2) {
		return julianDay(y1, m1, d1) - julianDay(y2, m2, d2);
	}
}
