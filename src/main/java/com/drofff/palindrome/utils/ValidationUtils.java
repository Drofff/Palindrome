package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.exception.ValidationException;

public class ValidationUtils {

	private static final String FIELD_ERROR_SUFFIX = "Error";

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	private ValidationUtils() {}

	public static <T> void validate(T obj) {
		Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(obj);
		Map<String, String> errorMap = violationsToErrorMap(violationSet);
		if(isNotEmpty(errorMap)) {
			throw new ValidationException(errorMap);
		}
	}

	private static <T> Map<String, String> violationsToErrorMap(Set<ConstraintViolation<T>> violations) {
		return violations.stream()
				.collect(
						Collectors.toMap(
								violation -> violation.getPropertyPath() + FIELD_ERROR_SUFFIX,
								ConstraintViolation::getMessage
						)
				);
	}

	private static boolean isNotEmpty(Map<?, ?> collection) {
		return !collection.isEmpty();
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
