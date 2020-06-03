package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.Hronable;
import com.drofff.palindrome.exception.PalindromeException;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.drofff.palindrome.utils.DateUtils.countHronablesPerDayForDays;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void countHronablesPerDayForDaysTest() {
        List<Hronable> hronablesList = initHronablesList();
        Map<LocalDate, Integer> hronablesPerDays = countHronablesPerDayForDays(hronablesList, 7);
        int hronablesTodayCount = getHronablesCountForToday(hronablesPerDays);
        assertEquals(1, hronablesTodayCount);
        int totalHronablesCount = getTotalCountOfHronables(hronablesPerDays);
        assertEquals(1, totalHronablesCount);
    }

    private List<Hronable> initHronablesList() {
        LocalDateTime now = LocalDateTime.now();
        return range(0, 8)
                .mapToObj(now::minusWeeks)
                .map(dateTime -> (Hronable) () -> dateTime)
                .collect(toList());
    }

    private int getHronablesCountForToday(Map<LocalDate, Integer> hronablesPerDays) {
        LocalDate today = LocalDate.now();
        return hronablesPerDays.keySet().stream()
                .filter(today::isEqual)
                .map(hronablesPerDays::get)
                .findFirst().orElseThrow(() -> new PalindromeException("Missing hronables count for today"));
    }

    private int getTotalCountOfHronables(Map<LocalDate, Integer> hronablesPerDays) {
        return hronablesPerDays.values().stream()
                .mapToInt(count -> count)
                .sum();
    }

}