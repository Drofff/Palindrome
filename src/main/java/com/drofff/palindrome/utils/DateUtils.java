package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.Hronable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.stream.IntStream.range;

public class DateUtils {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("kk:mm dd-MM-uuuu");

	private static final int MAX_HOUR = 23;
	private static final int MAX_MINUTE = 59;

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

	public static <T extends Hronable> Map<LocalDate, Integer> countHronablesPerDayForDays(List<T> hronables, int days) {
		Map<LocalDate, Integer> hronablesPerDayCounter = initHronablesPerDayCounterOfDays(days);
		countHronablesPerDayIntoCounter(hronables, hronablesPerDayCounter);
		return hronablesPerDayCounter;
	}

	private static Map<LocalDate, Integer> initHronablesPerDayCounterOfDays(int days) {
		LocalDate from = now().minusDays(days);
		Map<LocalDate, Integer> hronablesPerDayCounter = new LinkedHashMap<>();
		int offsetRightBound = days + 1;
		range(1, offsetRightBound)
				.mapToObj(from::plusDays)
				.forEach(date -> hronablesPerDayCounter.put(date, 0));
		return hronablesPerDayCounter;
	}

	private static <T extends Hronable> void countHronablesPerDayIntoCounter(List<T> hronables, Map<LocalDate, Integer> counter) {
		hronables.forEach(hronable -> {
			LocalDate date = hronable.getDateTime().toLocalDate();
			int hronablesCounter = counter.get(date);
			counter.put(date, ++hronablesCounter);
		});
	}

	public static LocalDateTime endOfDay(LocalDateTime dateTime) {
		return dateTime.withHour(MAX_HOUR)
				.withMinute(MAX_MINUTE)
				.truncatedTo(MINUTES);
	}

	public static boolean inSameMonth(LocalDate d0, LocalDate d1) {
		boolean haveSameYear = d0.getYear() == d1.getYear();
		boolean haveSameMonth = d0.getMonth() == d1.getMonth();
		return haveSameMonth && haveSameYear;
	}

}
