package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;

public interface ViolationService {

	List<Violation> getDriverViolations(Driver driver);

	Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable);

}
