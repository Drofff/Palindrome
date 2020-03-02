package com.drofff.palindrome.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("kk:mm dd-MM-uuuu");

	private DateUtils() {}

	public static LocalDate parseDateFromStr(String dateStr) {
		return LocalDate.parse(dateStr, DATE_FORMATTER);
	}

	public static String dateTimeToStr(LocalDateTime dateTime) {
		return dateTime.format(DATE_TIME_FORMATTER);
	}

	public static int dateTimeToEpochSeconds(LocalDateTime dateTime) {
		return (int) dateTime.toEpochSecond(ZoneOffset.UTC);
	}

}
