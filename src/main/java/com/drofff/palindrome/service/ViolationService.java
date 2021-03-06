package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.type.ViolationsStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ViolationService {

	List<Violation> getCarViolations(Car car);

	List<Violation> getDriverViolations(Driver driver);

	long countUnpaidViolationsOfDriver(Driver driver);

	Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable);

	Page<Violation> getPageOfViolationsAddedByPolice(Police police, Pageable pageable);

	Violation getViolationOfUserById(User user, String id);

	Violation getViolationById(String id);

	void markAsPaid(Violation violation);

	void addViolation(Violation violation);

	Map<LocalDate, Integer> countViolationsPerLastDays(int days);

	ViolationsStatistic getViolationsStatisticForDriver(Driver driver);

	boolean hasAnyViolationOfType(ViolationType violationType);

}