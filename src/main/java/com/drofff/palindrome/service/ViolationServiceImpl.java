package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ViolationRepository;
import com.drofff.palindrome.type.Mail;
import com.drofff.palindrome.type.ViolationsStatistic;
import com.drofff.palindrome.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.DateUtils.endOfDay;
import static com.drofff.palindrome.utils.DateUtils.inSameMonth;
import static com.drofff.palindrome.utils.ListUtils.*;
import static com.drofff.palindrome.utils.MailUtils.getViolationAddedMail;
import static com.drofff.palindrome.utils.ValidationUtils.*;
import static com.drofff.palindrome.utils.ViolationUtils.violationsDateTimeComparator;
import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@Service
public class ViolationServiceImpl implements ViolationService {

	private static final int MOST_FREQUENT_VIOLATION_TYPES_SIZE = 3;
	private static final int VIOLATIONS_COUNT_PER_MONTHS = 7;

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
		validateNotNullEntityHasId(car);
		return violationRepository.findByCarId(car.getId());
	}

	@Override
	public List<Violation> getDriverViolations(Driver driver) {
		validateNotNullEntityHasId(driver);
		return violationRepository.findByViolatorId(driver.getUserId());
	}

	@Override
	public Page<Violation> getPageOfDriverViolations(Driver driver, Pageable pageable) {
		validateNotNullEntityHasId(driver);
		return violationRepository.findByViolatorId(driver.getUserId(), pageable);
	}

	@Override
	public Page<Violation> getPageOfViolationsAddedByPolice(Police police, Pageable pageable) {
		validateNotNullEntityHasId(police);
		return violationRepository.findByOfficerIdOrderByDateTimeDesc(police.getId(), pageable);
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
		validateNotNullEntityHasId(violation);
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

	private void validateViolationTypeId(String id) {
		if(notExistsViolationTypeWithId(id)) {
			throw new ValidationException("Invalid violation type id");
		}
	}

	private boolean notExistsViolationTypeWithId(String id) {
		return !violationTypeService.existsViolationTypeWithId(id);
	}

	private void initViolationValues(Violation violation) {
		Police police = getCurrentPolice();
		violation.setOfficerId(police.getId());
		violation.setDateTime(now());
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
		ViolationType violationType = violationTypeService.getById(violation.getViolationTypeId());
		return violationType.getFee().getAmount() > 0;
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

	@Override
	public Map<LocalDate, Integer> countViolationsPerLastDays(int days) {
		validateIsPositive(days);
		List<Violation> violations = getViolationsOfLastDays(days);
		return DateUtils.countHronablesPerDayForDays(violations, days);
	}

	private void validateIsPositive(int days) {
		if(isNotPositiveNumber(days)) {
			throw new ValidationException("The number of days should be a positive number");
		}
	}

	private boolean isNotPositiveNumber(int number) {
		return number < 1;
	}

	private List<Violation> getViolationsOfLastDays(int days) {
		LocalDateTime startDay = now().minusDays(days);
		LocalDateTime threshold = endOfDay(startDay);
		String officerId = getCurrentPolice().getId();
		return violationRepository.findByDateTimeAfterAndOfficerId(threshold, officerId);
	}

	private Police getCurrentPolice() {
		User currentUser = getCurrentUser();
		return policeService.getPoliceByUserId(currentUser.getId());
	}

	@Override
	public ViolationsStatistic getViolationsStatisticForDriver(Driver driver) {
		validateNotNull(driver, "Driver should be provided");
		String violatorId = driver.getUserId();
		validateNotNull(violatorId, "Driver should obtain a user id");
		List<Violation> violations = violationRepository.findByViolatorId(violatorId);
		return isNotEmpty(violations) ? statisticOfViolations(violations) : new ViolationsStatistic();
	}

	private ViolationsStatistic statisticOfViolations(List<Violation> violations) {
		return new ViolationsStatistic.Builder()
				.violationsCount(violations.size())
				.unpaidViolationsCount(countElementsMatchingFilter(violations, this::isUnpaid))
				.longestUnpaidViolationDateTime(getLongestUnpaidViolationDateTime(violations))
				.mostFrequentViolationTypes(getMostFrequentViolationTypes(violations))
				.violationsCountPerLastMonths(countViolationsPerLastMonths(violations))
				.build();
	}

	private LocalDateTime getLongestUnpaidViolationDateTime(List<Violation> violations) {
		return violations.stream()
				.filter(this::isUnpaid)
				.min(violationsDateTimeComparator())
				.map(Violation::getDateTime)
				.orElse(null);
	}

	private boolean isUnpaid(Violation violation) {
		return !violation.isPaid();
	}

	private Map<ViolationType, Integer> getMostFrequentViolationTypes(List<Violation> violations) {
		return getMostFrequentViolationTypeIds(violations).entrySet().stream()
				.sorted(comparingInt(entry -> -entry.getValue()))
				.limit(MOST_FREQUENT_VIOLATION_TYPES_SIZE)
				.map(this::toViolationTypeFrequency)
				.collect(toMap(Pair::getFirst, Pair::getSecond));
	}

	private Map<String, Integer> getMostFrequentViolationTypeIds(List<Violation> violations) {
		List<String> violationTypeIds = applyToEachListElement(Violation::getViolationTypeId, violations);
		return violationTypeIds.stream().distinct()
				.collect(toFrequencyMap(violationTypeIds));
	}

	private Collector<String, ?, Map<String, Integer>> toFrequencyMap(List<String> source) {
		return toMap(id -> id, id -> (int) countElementsMatchingFilter(source, id::equals));
	}

	private Pair<ViolationType, Integer> toViolationTypeFrequency(Map.Entry<String, Integer> violationTypeIdFrequency) {
		String violationTypeId = violationTypeIdFrequency.getKey();
		ViolationType violationType = violationTypeService.getById(violationTypeId);
		return Pair.of(violationType, violationTypeIdFrequency.getValue());
	}

	private Map<LocalDate, Integer> countViolationsPerLastMonths(List<Violation> violations) {
		Map<LocalDate, Integer> violationsPerMonths = new LinkedHashMap<>();
		getLastMonths().forEach(month -> {
			Predicate<Violation> isViolationOfMonth = violation -> isViolationOfMonth(violation, month);
			int violationsPerMonth = (int) countElementsMatchingFilter(violations, isViolationOfMonth);
			violationsPerMonths.put(month, violationsPerMonth);
		});
		return violationsPerMonths;
	}

	private List<LocalDate> getLastMonths() {
		LocalDate startMonth = LocalDate.now().minusMonths(VIOLATIONS_COUNT_PER_MONTHS);
		int maxMonthOffset = VIOLATIONS_COUNT_PER_MONTHS + 1;
		return range(1, maxMonthOffset)
				.mapToObj(startMonth::plusMonths)
				.collect(toList());
	}

	private boolean isViolationOfMonth(Violation violation, LocalDate dateOfMonth) {
		LocalDate dateOfViolation = violation.getDateTime().toLocalDate();
		return inSameMonth(dateOfViolation, dateOfMonth);
	}

	@Override
	public boolean hasAnyViolationOfType(ViolationType violationType) {
		validateNotNullEntityHasId(violationType);
		return violationRepository.existsByViolationTypeId(violationType.getId());
	}

}