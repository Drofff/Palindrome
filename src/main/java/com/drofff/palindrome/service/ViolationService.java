package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ViolationService {

	List<Violation> getCarViolations(Car car);

	List<Violation> getDriverViolations(Driver driver);

	Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable);

	Violation getViolationOfUserById(User user, String id);

	Violation getViolationById(String id);

	void markAsPaid(Violation violation);

	void addViolation(Violation violation);

}
