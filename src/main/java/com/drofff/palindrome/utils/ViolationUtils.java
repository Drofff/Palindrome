package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.DateUtils.dateTimeToEpochSeconds;
import static java.util.Comparator.comparingInt;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

import com.drofff.palindrome.document.Violation;

public class ViolationUtils {

	private ViolationUtils() {}

	public static Optional<LocalDateTime> getLatestViolationDateTimeIfPresent(List<Violation> violations) {
		return violations.stream()
				.max(violationsByDateTimeComparator())
				.map(Violation::getDateTime);
	}

	public static long countUnpaidViolations(List<Violation> violations) {
		return violations.size() - countPaidViolations(violations);
	}

	public static long countPaidViolations(List<Violation> violations) {
		return violations.stream()
				.filter(Violation::isPaid)
				.count();
	}

	public static Comparator<Violation> violationsByDateTimeComparator() {
		ToIntFunction<Violation> violationDateTimeToIntFunction = violation -> dateTimeToEpochSeconds(violation.getDateTime());
		return comparingInt(violationDateTimeToIntFunction);
	}

}
