package com.drofff.palindrome.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

	private DateUtils() {}

	public static LocalDate parseDateFromStr(String dateStr) {
		return LocalDate.parse(dateStr, DATE_FORMATTER);
	}

}
