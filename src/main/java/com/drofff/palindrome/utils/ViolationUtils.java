package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.DateUtils.dateTimeToEpochSeconds;
import static java.util.Comparator.comparingInt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.drofff.palindrome.document.Violation;

public class ViolationUtils {

	private ViolationUtils() {}

	public static Optional<LocalDateTime> getLatestViolationDateTimeIfPresent(List<Violation> violations) {
		return violations.stream()
				.max(comparingInt(violation -> dateTimeToEpochSeconds(violation.getDateTime())))
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

}
