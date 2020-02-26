package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.ReflectionUtils.getFieldFromClassByName;
import static com.drofff.palindrome.utils.ReflectionUtils.getFieldValueFromObject;
import static com.drofff.palindrome.utils.StringUtils.isNotBlank;
import static com.drofff.palindrome.utils.StringUtils.isNotString;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.drofff.palindrome.annotation.Filter;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.annotation.TargetField;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.filter.ComparisonStrategy;
import com.drofff.palindrome.filter.EqualsComparisonStrategy;

public class FilterUtils {

	private static final ComparisonStrategy DEFAULT_COMPARISON_STRATEGY = new EqualsComparisonStrategy();

	private FilterUtils() {}

	public static <T, F> List<T> filter(List<T> objects, F filter) {
		if(isNotEmpty(objects)) {
			validateHasFilterAnnotation(filter);
			Class<?> targetClass = objects.get(0).getClass();
			validateFilterIsApplicableToClass(filter, targetClass);
			return filterObjects(objects, filter);
		}
		return objects;
	}

	private static boolean isNotEmpty(List<?> list) {
		return !list.isEmpty();
	}

	private static <F> void validateHasFilterAnnotation(F filter) {
		if(hasNoFilterAnnotation(filter)) {
			throw new PalindromeException(filter.getClass().getName() + " is not a filter");
		}
	}

	private static <F> boolean hasNoFilterAnnotation(F filter) {
		Filter filterAnnotation = filter.getClass().getAnnotation(Filter.class);
		return Objects.isNull(filterAnnotation);
	}

	private static <T, F> void validateFilterIsApplicableToClass(F filter, Class<T> targetClass) {
		if(isFilterNotApplicableToClass(filter, targetClass)) {
			throw new PalindromeException("Filter " + filter.getClass().getName() + " is not applicable to class " + targetClass.getName());
		}
	}

	private static <F> boolean isFilterNotApplicableToClass(F filter, Class<?> targetClass) {
		return !isFilterApplicableToClass(filter, targetClass);
	}

	private static <F> boolean isFilterApplicableToClass(F filter, Class<?> targetClass) {
		Filter filterAnnotation = filter.getClass().getAnnotation(Filter.class);
		Class<?> annotatedTargetClass = filterAnnotation.forClass();
		return annotatedTargetClass.equals(targetClass);
	}

	private static <T, F> List<T> filterObjects(List<T> objects, F filter) {
		return objects.stream()
				.filter(object -> matchesFilter(object, filter))
				.collect(Collectors.toList());
	}

	private static <T, F> boolean matchesFilter(T target, F filter) {
		Field[] filterFields = filter.getClass().getDeclaredFields();
		return Arrays.stream(filterFields)
				.filter(field -> hasNonNullFieldValue(field, filter))
				.allMatch(field -> matchesFieldFilter(field, target, filter));
	}

	private static <F> boolean hasNonNullFieldValue(Field filterField, F filter) {
		Object fieldValue = getFieldValueFromObject(filterField, filter);
		return Objects.nonNull(fieldValue) && isNotBlankIfStr(fieldValue);
	}

	private static boolean isNotBlankIfStr(Object object) {
		return isNotString(object) || isNotBlank((String) object);
	}

	private static <T, F> boolean matchesFieldFilter(Field filterField, T target, F filter) {
		Object targetFieldValue = getTargetFilterFieldValue(filterField, target);
		Object filterFieldValue = getFieldValueFromObject(filterField, filter);
		ComparisonStrategy strategy = getComparisonStrategyForField(filterField);
		return strategy.compare(targetFieldValue, filterFieldValue);
	}

	private static <T> Object getTargetFilterFieldValue(Field filterField, T target) {
		String targetFieldName = getTargetFieldName(filterField);
		return getValueOfFieldWithName(targetFieldName, target);
	}

	private static String getTargetFieldName(Field filterField) {
		if(hasTargetFieldAnnotation(filterField)) {
			TargetField targetField = filterField.getAnnotation(TargetField.class);
			return targetField.name();
		}
		return filterField.getName();
	}

	private static boolean hasTargetFieldAnnotation(Field field) {
		return field.getAnnotation(TargetField.class) != null;
	}

	private static <T> Object getValueOfFieldWithName(String fieldName, T target) {
		Class<?> targetClass = target.getClass();
		Field targetField = getFieldFromClassByName(fieldName, targetClass)
				.orElseThrow(() -> new PalindromeException("Can not resolve field with name " + fieldName + " in class " + targetClass.getName()));
		return getFieldValueFromObject(targetField, target);
	}

	private static ComparisonStrategy getComparisonStrategyForField(Field field) {
		Strategy strategy = field.getAnnotation(Strategy.class);
		return Objects.isNull(strategy) ? DEFAULT_COMPARISON_STRATEGY : comparisonStrategyOfClass(strategy.value());
	}

	private static ComparisonStrategy comparisonStrategyOfClass(Class<? extends ComparisonStrategy> strategyClass) {
		try {
			return strategyClass.getConstructor().newInstance();
		} catch(Exception e) {
			throw new PalindromeException("Can not instantiate an instance of class " + strategyClass.getName());
		}
	}

}
