package com.drofff.palindrome.service;

import static com.drofff.palindrome.enums.Role.ADMIN;
import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.MailUtils.getChangeRequestApprovedDriverMail;
import static com.drofff.palindrome.utils.MailUtils.getChangeRequestApprovedSenderMail;
import static com.drofff.palindrome.utils.MailUtils.getChangeRequestRefusedMail;
import static com.drofff.palindrome.utils.ReflectionUtils.classByName;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateCurrentUserHasRole;
import static com.drofff.palindrome.utils.ValidationUtils.validateEntityHasId;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ChangeRequestRepository;
import com.drofff.palindrome.type.Mail;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService {

	private final ChangeRequestRepository changeRequestRepository;
	private final MailService mailService;
	private final PoliceService policeService;
	private final DriverService driverService;
	private final AuthenticationService authenticationService;
	private final List<EntityManager> entityManagers;

	@Autowired
	public ChangeRequestServiceImpl(ChangeRequestRepository changeRequestRepository, MailService mailService,
	                                PoliceService policeService, DriverService driverService,
	                                AuthenticationService authenticationService, List<EntityManager> entityManagers) {
		this.changeRequestRepository = changeRequestRepository;
		this.mailService = mailService;
		this.policeService = policeService;
		this.driverService = driverService;
		this.authenticationService = authenticationService;
		this.entityManagers = entityManagers;
	}

	@Override
	public void requestDriverInfoChangeWithComment(Driver driver, String comment) {
		validateCurrentUserHasRole(POLICE);
		validate(driver);
		validateEntityHasId(driver);
		ChangeRequest changeRequest = changeRequestForDriverWithComment(driver, comment);
		changeRequestRepository.save(changeRequest);
	}

	private ChangeRequest changeRequestForDriverWithComment(Driver driver, String comment) {
		return changeRequestWithComment(comment)
				.forDriver(driver)
				.targetOwnerId(driver.getUserId())
				.build();
	}

	@Override
	public void requestCarInfoChangeWithComment(Car car, String comment) {
		validateCurrentUserHasRole(POLICE);
		validate(car);
		validateEntityHasId(car);
		ChangeRequest changeRequest = changeRequestForCarWithComment(car, comment);
		changeRequestRepository.save(changeRequest);
	}

	private ChangeRequest changeRequestForCarWithComment(Car car, String comment) {
		Driver carOwner = driverService.getOwnerOfCar(car);
		return changeRequestWithComment(comment)
				.forCar(car)
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
		Driver driver = driverService.getDriverByUserId(changeRequest.getSenderId());
		return getChangeRequestApprovedDriverMail(driver.getFirstName());
	}

	private void sendMailToRequestTargetOwner(Mail mail, ChangeRequest changeRequest) {
		User targetOwner = authenticationService.getUserById(changeRequest.getTargetOwnerId());
		mailService.sendMailTo(mail, targetOwner.getUsername());
	}

	private void notifySenderChangeRequestApproved(ChangeRequest changeRequest) {
		Mail requestApprovedMail = getApprovedSenderMailForRequest(changeRequest);
		sendMailToRequestSender(requestApprovedMail, changeRequest);
	}

	private Mail getApprovedSenderMailForRequest(ChangeRequest changeRequest) {
		Police police = policeService.getPoliceByUserId(changeRequest.getSenderId());
		User targetOwner = authenticationService.getUserById(changeRequest.getSenderId());
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
		User targetOwner = authenticationService.getUserById(changeRequest.getTargetOwnerId());
		return getChangeRequestRefusedMail(police.getFirstName(), targetOwner.getUsername());
	}

	private void sendMailToRequestSender(Mail mail, ChangeRequest request) {
		User sender = authenticationService.getUserById(request.getSenderId());
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
	public ChangeRequest getDriverChangeRequestById(String id) {
		return getChangeRequestByIdAndTargetClass(id, Driver.class);
	}


	@Override
	public ChangeRequest getCarChangeRequestById(String id) {
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

}
