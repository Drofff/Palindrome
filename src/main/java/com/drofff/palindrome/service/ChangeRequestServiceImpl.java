package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ChangeRequestRepository;
import com.drofff.palindrome.type.Mail;
import com.drofff.palindrome.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.drofff.palindrome.enums.Role.ADMIN;
import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.MailUtils.*;
import static com.drofff.palindrome.utils.ReflectionUtils.classByName;
import static com.drofff.palindrome.utils.ValidationUtils.*;
import static java.time.LocalDateTime.now;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService {

	private final ChangeRequestRepository changeRequestRepository;
	private final MailService mailService;
	private final PoliceService policeService;
	private final DriverService driverService;
	private final UserService userService;
	private final List<EntityManager> entityManagers;

	@Autowired
	public ChangeRequestServiceImpl(ChangeRequestRepository changeRequestRepository, MailService mailService,
	                                PoliceService policeService, DriverService driverService,
									UserService userService, List<EntityManager> entityManagers) {
		this.changeRequestRepository = changeRequestRepository;
		this.mailService = mailService;
		this.policeService = policeService;
		this.driverService = driverService;
		this.userService = userService;
		this.entityManagers = entityManagers;
	}

	@Override
	public void requestDriverInfoChangeWithComment(Driver updatedDriver, String comment) {
		validateCurrentUserHasRole(POLICE);
		validate(updatedDriver);
		validateEntityHasId(updatedDriver);
		ChangeRequest changeRequest = driverInfoUpdateRequestWithComment(updatedDriver, comment);
		changeRequestRepository.save(changeRequest);
	}

	private ChangeRequest driverInfoUpdateRequestWithComment(Driver updatedDriver, String comment) {
		Driver originalDriver = driverService.getDriverById(updatedDriver.getId());
		return changeRequestWithComment(comment)
				.withDriverUpdate(updatedDriver)
				.targetOwnerId(originalDriver.getUserId())
				.build();
	}

	@Override
	public void requestCarInfoChangeWithComment(Car updatedCar, String comment) {
		validateCurrentUserHasRole(POLICE);
		validate(updatedCar);
		validateEntityHasId(updatedCar);
		ChangeRequest changeRequest = carInfoUpdateRequestWithComment(updatedCar, comment);
		changeRequestRepository.save(changeRequest);
	}

	private ChangeRequest carInfoUpdateRequestWithComment(Car updatedCar, String comment) {
		Driver carOwner = driverService.getOwnerOfCar(updatedCar);
		return changeRequestWithComment(comment)
				.withCarUpdate(updatedCar)
				.targetOwnerId(carOwner.getUserId())
				.build();
	}

	private ChangeRequest.Builder changeRequestWithComment(String comment) {
		String currentUserId = getCurrentUser().getId();
		return new ChangeRequest.Builder()
				.comment(comment)
				.senderId(currentUserId)
				.now();
	}

	@Override
	public void approveChangeById(String id) {
		validateCurrentUserHasRole(ADMIN);
		ChangeRequest changeRequest = getNotApprovedRequestById(id);
		applyChange(changeRequest);
		approveChangeRequest(changeRequest);
		notifyChangeRequestApproved(changeRequest);
	}

	private void applyChange(ChangeRequest changeRequest) {
		Class<?> targetClass = classByName(changeRequest.getTargetClassName());
		EntityManager entityManager = getEntityManagerForClass(targetClass);
		Entity entity = changeRequest.getTargetValue();
		entityManager.update(entity);
	}

	private EntityManager getEntityManagerForClass(Class<?> clazz) {
		return entityManagers.stream()
				.filter(entityManager -> entityManager.isManagingEntityOfClass(clazz))
				.findFirst()
				.orElseThrow(() -> new ValidationException("Can not resolve manager for class " + clazz.getName()));
	}

	private void approveChangeRequest(ChangeRequest changeRequest) {
		changeRequest.setApproved(true);
		changeRequest.setDateTime(now());
		changeRequestRepository.save(changeRequest);
	}

	private void notifyChangeRequestApproved(ChangeRequest changeRequest) {
		notifyDriverChangeRequestApproved(changeRequest);
		notifySenderChangeRequestApproved(changeRequest);
	}

	private void notifyDriverChangeRequestApproved(ChangeRequest changeRequest) {
		Mail requestApprovedMail = getApprovedDriverMailForRequest(changeRequest);
		sendMailToRequestTargetOwner(requestApprovedMail, changeRequest);
	}

	private Mail getApprovedDriverMailForRequest(ChangeRequest changeRequest) {
		Driver driver = driverService.getDriverByUserId(changeRequest.getTargetOwnerId());
		return getChangeRequestApprovedDriverMail(driver.getFirstName());
	}

	private void sendMailToRequestTargetOwner(Mail mail, ChangeRequest changeRequest) {
		User targetOwner = userService.getUserById(changeRequest.getTargetOwnerId());
		mailService.sendMailTo(mail, targetOwner.getUsername());
	}

	private void notifySenderChangeRequestApproved(ChangeRequest changeRequest) {
		Mail requestApprovedMail = getApprovedSenderMailForRequest(changeRequest);
		sendMailToRequestSender(requestApprovedMail, changeRequest);
	}

	private Mail getApprovedSenderMailForRequest(ChangeRequest changeRequest) {
		Police police = policeService.getPoliceByUserId(changeRequest.getSenderId());
		User targetOwner = userService.getUserById(changeRequest.getTargetOwnerId());
		return getChangeRequestApprovedSenderMail(police.getFirstName(), targetOwner.getUsername());
	}

	@Override
	public void refuseChangeById(String id) {
		validateCurrentUserHasRole(ADMIN);
		ChangeRequest changeRequest = getNotApprovedRequestById(id);
		changeRequestRepository.delete(changeRequest);
		notifyChangeRequestRefuse(changeRequest);
	}

	private void notifyChangeRequestRefuse(ChangeRequest changeRequest) {
		Mail requestRefuseMail = getRefuseMailForRequest(changeRequest);
		sendMailToRequestSender(requestRefuseMail, changeRequest);
	}

	private Mail getRefuseMailForRequest(ChangeRequest changeRequest) {
		Police police = policeService.getPoliceByUserId(changeRequest.getSenderId());
		User targetOwner = userService.getUserById(changeRequest.getTargetOwnerId());
		return getChangeRequestRefusedMail(police.getFirstName(), targetOwner.getUsername());
	}

	private void sendMailToRequestSender(Mail mail, ChangeRequest request) {
		User sender = userService.getUserById(request.getSenderId());
		mailService.sendMailTo(mail, sender.getUsername());
	}

	@Override
	public List<ChangeRequest> getAllChangeRequests() {
		return changeRequestRepository.findAll().stream()
				.filter(this::isNotApproved)
				.collect(Collectors.toList());
	}

	private boolean isNotApproved(ChangeRequest changeRequest) {
		return !changeRequest.isApproved();
	}

	@Override
	public Page<ChangeRequest> getPageOfChangeRequestsSentBy(Police sender, Pageable pageable) {
		validateNotNull(sender, "Sender should be provided");
		return changeRequestRepository.findBySenderIdOrderByDateTimeDesc(sender.getUserId(), pageable);
	}

	@Override
	public ChangeRequest getDriverChangeRequestById(String id) {
		validateNotNull(id, "Driver change request id is required");
		return getChangeRequestByIdAndTargetClass(id, Driver.class);
	}

	@Override
	public ChangeRequest getCarChangeRequestById(String id) {
		validateNotNull(id, "Car change request id is required");
		return getChangeRequestByIdAndTargetClass(id, Car.class);
	}

	private ChangeRequest getChangeRequestByIdAndTargetClass(String id, Class<? extends Entity> targetClass) {
		ChangeRequest changeRequest = getNotApprovedRequestById(id);
		validateChangeRequestHasTargetClass(changeRequest, targetClass);
		return changeRequest;
	}

	private void validateChangeRequestHasTargetClass(ChangeRequest changeRequest, Class<? extends Entity> targetClass) {
		if(notHasTargetClass(changeRequest, targetClass)) {
			throw new ValidationException("Change request target class is not " + targetClass.getName());
		}
	}

	private boolean notHasTargetClass(ChangeRequest changeRequest, Class<? extends Entity> targetClass) {
		return !hasTargetClass(changeRequest, targetClass);
	}

	private boolean hasTargetClass(ChangeRequest changeRequest, Class<? extends Entity> targetClass) {
		String targetClassName = targetClass.getName();
		String requestTargetClassName = changeRequest.getTargetClassName();
		return targetClassName.equals(requestTargetClassName);
	}

	private ChangeRequest getNotApprovedRequestById(String id) {
		return changeRequestRepository.findById(id)
				.filter(this::isNotApproved)
				.orElseThrow(() -> new ValidationException("Change request with such id doesn't exist"));
	}

	@Override
	public int countChangeRequests() {
		return changeRequestRepository.countNotApproved();
	}

	@Override
	public Map<LocalDate, Integer> countApprovedChangeRequestsPerLastDays(int days) {
		List<ChangeRequest> approvedChangeRequests = getApprovedChangeRequestsOfLastDays(days);
		return DateUtils.countHronablesPerDayForDays(approvedChangeRequests, days);
	}

	private List<ChangeRequest> getApprovedChangeRequestsOfLastDays(int days) {
		LocalDateTime threshold = now().minusDays(days);
		String senderId = getCurrentUser().getId();
		return changeRequestRepository.findByDateTimeAfterAndApprovedAndSenderId(threshold, true, senderId);
	}

}
