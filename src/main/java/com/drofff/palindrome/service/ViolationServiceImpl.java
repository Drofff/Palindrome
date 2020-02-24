package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.repository.ViolationRepository;

@Service
public class ViolationServiceImpl implements ViolationService {

	private final ViolationRepository violationRepository;

	@Autowired
	public ViolationServiceImpl(ViolationRepository violationRepository) {
		this.violationRepository = violationRepository;
	}

	@Override
	public List<Violation> getDriverViolations(Driver driver) {
		validateDriver(driver);
		return violationRepository.findByViolatorId(driver.getId());
	}

	@Override
	public Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable) {
		validateDriver(driver);
		return violationRepository.findByViolatorId(driver.getId(), pageable);
	}

	private void validateDriver(Driver driver) {
		validateNotNull(driver, "Driver should be provided");
		validateNotNull(driver.getId(), "Driver should obtain id");
	}

}
