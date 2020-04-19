package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ViolationRepository;
import com.drofff.palindrome.type.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.MailUtils.getViolationAddedMail;
import static com.drofff.palindrome.utils.ValidationUtils.*;

@Service
public class ViolationServiceImpl implements ViolationService {

	private final ViolationRepository violationRepository;
	private final PoliceService policeService;
	private final ViolationTypeService violationTypeService;
	private final UserService userService;
	private final MailService mailService;

	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	public ViolationServiceImpl(ViolationRepository violationRepository, PoliceService policeService,
	                            ViolationTypeService violationTypeService, UserService userService,
	                            MailService mailService) {
		this.violationRepository = violationRepository;
		this.policeService = policeService;
		this.violationTypeService = violationTypeService;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public List<Violation> getCarViolations(Car car) {
		validateNotNull(car);
		validateEntityHasId(car);
		return violationRepository.findByCarId(car.getId());
	}

	@Override
	public List<Violation> getDriverViolations(Driver driver) {
		validateNotNull(driver);
		validateEntityHasId(driver);
		return violationRepository.findByViolatorId(driver.getUserId());
	}

	@Override
	public Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable) {
		validateNotNull(driver);
		validateEntityHasId(driver);
		return violationRepository.findByViolatorId(driver.getUserId(), pageable);
	}

	@Override
	public Violation getViolationOfUserById(User user, String id) {
		validateNotNull(id, "Violation id is required");
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
		validateNotNull(violation);
		validateEntityHasId(violation);
		User currentUser = getCurrentUser();
		validateUserIsViolatorOf(currentUser, violation);
		violation.setPaid(true);
		violationRepository.save(violation);
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

	@Override
	public void addViolation(Violation violation) {
		validateCurrentUserHasRole(POLICE);
		validate(violation);
		validateViolationTypeId(violation.getViolationTypeId());
		initViolationValues(violation);
		violationRepository.save(violation);
		notifyViolationAdded(violation);
	}

	private void initViolationValues(Violation violation) {
		Police police = getCurrentPolice();
		violation.setOfficerId(police.getId());
		violation.setDateTime(LocalDateTime.now());
		initViolationPaidStatus(violation);
	}

	private void initViolationPaidStatus(Violation violation) {
		boolean isPaid = hasNoFee(violation);
		violation.setPaid(isPaid);
	}

	private boolean hasNoFee(Violation violation) {
		return !hasFee(violation);
	}

	private boolean hasFee(Violation violation) {
		ViolationType violationType = violationTypeService.getViolationTypeById(violation.getViolationTypeId());
		return violationType.getFee().getAmount() > 0;
	}

	private Police getCurrentPolice() {
		User currentUser = getCurrentUser();
		return policeService.getPoliceByUserId(currentUser.getId());
	}

	private void validateViolationTypeId(String id) {
		validateNotNull(id, "Violation type is required");
		if(notExistsViolationTypeWithId(id)) {
			throw new ValidationException("Invalid violation type id");
		}
	}

	private boolean notExistsViolationTypeWithId(String id) {
		return !violationTypeService.existsViolationTypeWithId(id);
	}

	private void notifyViolationAdded(Violation violation) {
		User violator = userService.getUserById(violation.getViolatorId());
		String linkToViolation = generateLinkToViolationWithId(violation.getId());
		Mail notificationMail = getViolationAddedMail(linkToViolation);
		mailService.sendMailTo(notificationMail, violator.getUsername());
	}

	private String generateLinkToViolationWithId(String id) {
		return applicationUrl + "/violation/" + id;
	}

}