package com.drofff.palindrome.service;

import static com.drofff.palindrome.enums.Role.ADMIN;
import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ReflectionUtils.classByName;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateCurrentUserHasRole;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ChangeRequestRepository;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService {

	private final ChangeRequestRepository changeRequestRepository;
	private final List<EntityManager> entityManagers;

	@Autowired
	public ChangeRequestServiceImpl(ChangeRequestRepository changeRequestRepository, List<EntityManager> entityManagers) {
		this.changeRequestRepository = changeRequestRepository;
		this.entityManagers = entityManagers;
	}

	@Override
	public void requestDriverInfoChangeWithComment(Driver driver, String comment) {
		validateCurrentUserHasRole(POLICE);
		validateHasId(driver);
		validate(driver);
		ChangeRequest changeRequest = changeRequestForDriverWithComment(driver, comment);
		changeRequestRepository.save(changeRequest);
	}

	private ChangeRequest changeRequestForDriverWithComment(Driver driver, String comment) {
		return changeRequestWithComment(comment).forDriver(driver).build();
	}

	@Override
	public void requestCarInfoChangeWithComment(Car car, String comment) {
		validateCurrentUserHasRole(POLICE);
		validateHasId(car);
		validate(car);
		ChangeRequest changeRequest = changeRequestForCarWithComment(car, comment);
		changeRequestRepository.save(changeRequest);
	}

	private void validateHasId(Entity entity) {
		if(hasNoId(entity)) {
			throw new ValidationException("Entity should be provided with id");
		}
	}

	private boolean hasNoId(Entity entity) {
		return !hasId(entity);
	}

	private boolean hasId(Entity entity) {
		return Objects.nonNull(entity.getId());
	}

	private ChangeRequest changeRequestForCarWithComment(Car car, String comment) {
		return changeRequestWithComment(comment).forCar(car).build();
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

	@Override
	public void refuseChangeById(String id) {
		validateCurrentUserHasRole(ADMIN);
		ChangeRequest changeRequest = getNotApprovedRequestById(id);
		changeRequestRepository.delete(changeRequest);
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
