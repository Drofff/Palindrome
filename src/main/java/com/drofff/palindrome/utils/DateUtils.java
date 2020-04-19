package com.drofff.palindrome.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

	public static Date localDateTimeToDate(LocalDateTime dateTime) {
		ZoneOffset offset = ZonedDateTime.now().getOffset();
		Instant instant = dateTime.toInstant(offset);
		return Date.from(instant);
	}

}
