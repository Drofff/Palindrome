package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.Violation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

import static com.drofff.palindrome.utils.DateUtils.dateTimeToEpochSeconds;
import static java.util.Comparator.comparingInt;

public class ViolationUtils {

	private ViolationUtils() {}

	public static Optional<LocalDateTime> getLatestViolationDateTimeIfPresent(List<Violation> violations) {
		return violations.stream()
				.max(violationsDateTimeComparator())
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

	public static Comparator<Violation> violationsDateTimeComparator() {
		ToIntFunction<Violation> violationDateTimeToIntAsc = violation -> dateTimeToEpochSeconds(violation.getDateTime());
		return comparingInt(violationDateTimeToIntAsc);
	}

	public static Comparator<Violation> invertedViolationsDateTimeComparator() {
		ToIntFunction<Violation> violationDateTimeToIntDesc = violation -> -dateTimeToEpochSeconds(violation.getDateTime());
		return comparingInt(violationDateTimeToIntDesc);
	}

}
