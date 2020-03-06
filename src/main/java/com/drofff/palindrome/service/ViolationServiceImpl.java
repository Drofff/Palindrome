package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.exception.ValidationException;
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

	@Override
	public Violation getViolationOfUserById(User user, String id) {
		Violation violation = getViolationById(id);
		validateUserIsViolatorOf(user, violation);
		return violation;
	}

	public Violation getViolationById(String id) {
		return violationRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Violation with such id doesn't exist"));
	}

	@Override
	public void markAsPaid(Violation violation) {
		validateHasId(violation);
		User currentUser = getCurrentUser();
		validateUserIsViolatorOf(currentUser, violation);
		violation.setPaid(true);
		violationRepository.save(violation);
	}

	private void validateHasId(Violation violation) {
		if(hasNoId(violation)) {
			throw new ValidationException("Violation should obtain id");
		}
	}

	private boolean hasNoId(Violation violation) {
		return !hasId(violation);
	}

	private boolean hasId(Violation violation) {
		return Objects.nonNull(violation.getId());
	}

	private void validateUserIsViolatorOf(User user, Violation violation) {
		if(isNotViolatorOf(user, violation)) {
			throw new ValidationException("User is not related to the violation");
		}
	}

	private boolean isNotViolatorOf(User user, Violation violation) {
		return !isViolatorOf(user, violation);
	}

	private boolean isViolatorOf(User user, Violation violation) {
		return violation.getViolatorId().equals(user.getId());
	}

}
