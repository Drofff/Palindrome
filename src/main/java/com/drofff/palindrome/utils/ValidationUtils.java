package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static java.util.Arrays.stream;

public class ValidationUtils {

	private static final String FIELD_ERROR_SUFFIX = "Error";

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	private ValidationUtils() {}

	public static <T> void validate(T object) {
		validateNotNull(object);
		validateFieldValues(object);
	}

	private static <T> void validateFieldValues(T object) {
		Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(object);
		Map<String, String> errorMap = violationsToErrorMap(violationSet);
		if(isNotEmpty(errorMap)) {
			throw new ValidationException(errorMap);
		}
	}

	private static <T> Map<String, String> violationsToErrorMap(Set<ConstraintViolation<T>> violations) {
		return violations.stream()
				.collect(toErrorsMapCollector());
	}

	private static <T> Collector<ConstraintViolation<T>, ?, Map<String, String>> toErrorsMapCollector() {
		return Collectors.toMap(violation -> violation.getPropertyPath() + FIELD_ERROR_SUFFIX,
				ConstraintViolation::getMessage);
	}

	private static boolean isNotEmpty(Map<?, ?> collection) {
		return !collection.isEmpty();
	}

	public static <T extends Entity> void validateNotNullEntityHasId(T entity) {
		validateNotNull(entity);
		validateEntityHasId(entity);
	}

	public static <T extends Entity> void validateEntityHasId(T entity) {
		String entityName = getSimpleClassName(entity);
		validateNotNull(entity.getId(), entityName + " should obtain an id");
	}

	public static void validateAllAreNotNull(String errorMessage, Object ... objects) {
		stream(objects)
				.forEach(object -> validateNotNull(object, errorMessage));
	}

	public static void validateNotNull(Object object) {
		String className = getSimpleClassName(object);
		validateNotNull(object, className + " is required");
	}

	private static <T> String getSimpleClassName(T object) {
		return object.getClass().getSimpleName();
	}

	public static void validateNotNull(Object object, String errorMessage) {
		if(Objects.isNull(object)) {
			throw new ValidationException(errorMessage);
		}
	}

	public static void validateCurrentUserHasRole(Role role) {
		if(currentUserNotObtainRole(role)) {
			throw new ValidationException("User should obtain " + role.name() + " role");
		}
	}

	private static boolean currentUserNotObtainRole(Role role) {
		return !currentUserObtainRole(role);
	}

	private static boolean currentUserObtainRole(Role role) {
		return getCurrentUser().getAuthorities().contains(role);
	}

}
