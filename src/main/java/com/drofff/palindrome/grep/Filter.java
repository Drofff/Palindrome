package com.drofff.palindrome.grep;

import static com.drofff.palindrome.utils.ReflectionUtils.getFieldFromClassByName;
import static com.drofff.palindrome.utils.ReflectionUtils.getFieldValueFromObject;
import static com.drofff.palindrome.utils.ReflectionUtils.hasNonNullFieldValue;
import static java.util.Objects.isNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.annotation.TargetField;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.grep.strategy.ComparisonStrategy;
import com.drofff.palindrome.grep.strategy.EqualsComparisonStrategy;

public class Filter {

	private static final ComparisonStrategy DEFAULT_COMPARISON_STRATEGY = new EqualsComparisonStrategy();

	private Filter() {}

	public static <T, P> List<T> grepByPattern(List<T> objects, P pattern) {
		if(isNotEmpty(objects)) {
			validateHasPatternAnnotation(pattern);
			Class<?> targetClass = objects.get(0).getClass();
			validatePatternIsForClass(pattern, targetClass);
			return filterObjectsByPattern(objects, pattern);
		}
		return objects;
	}

	private static boolean isNotEmpty(List<?> list) {
		return !list.isEmpty();
	}

	private static <P> void validateHasPatternAnnotation(P pattern) {
		if(hasNoPatternAnnotation(pattern)) {
			throw new PalindromeException(pattern.getClass().getName() + " is not a pattern");
		}
	}

	private static <P> boolean hasNoPatternAnnotation(P pattern) {
		Pattern patternAnnotation = pattern.getClass().getAnnotation(Pattern.class);
		return isNull(patternAnnotation);
	}

	private static <T, P> void validatePatternIsForClass(P pattern, Class<T> targetClass) {
		if(isNotPatternForClass(pattern, targetClass)) {
			throw new PalindromeException("Pattern " + pattern.getClass().getName() + " is not applicable to class " + targetClass.getName());
		}
	}

	private static <P> boolean isNotPatternForClass(P pattern, Class<?> targetClass) {
		return !isPatternForClass(pattern, targetClass);
	}

	private static <P> boolean isPatternForClass(P pattern, Class<?> targetClass) {
		Pattern patternAnnotation = pattern.getClass().getAnnotation(Pattern.class);
		Class<?> annotatedTargetClass = patternAnnotation.forClass();
		return annotatedTargetClass.equals(targetClass);
	}

	private static <T, P> List<T> filterObjectsByPattern(List<T> objects, P pattern) {
		return objects.stream()
				.filter(object -> matchesPattern(object, pattern))
				.collect(Collectors.toList());
	}

	private static <T, P> boolean matchesPattern(T target, P pattern) {
		Field[] patternFields = pattern.getClass().getDeclaredFields();
		return Arrays.stream(patternFields)
				.filter(field -> hasNonNullFieldValue(pattern, field))
				.allMatch(field -> hasFieldMatchingWithPattern(target, field, pattern));
	}

	private static <T, P> boolean hasFieldMatchingWithPattern(T target, Field field, P pattern) {
		Object fieldValue = getTargetFieldValue(field, target);
		Object patternFieldValue = getFieldValueFromObject(field, pattern);
		ComparisonStrategy strategy = getComparisonStrategyForField(field);
		return strategy.compare(fieldValue, patternFieldValue);
	}

	private static <T> Object getTargetFieldValue(Field field, T target) {
		String targetFieldName = getTargetFieldName(field);
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
		return isNull(strategy) ? DEFAULT_COMPARISON_STRATEGY : comparisonStrategyOfClass(strategy.value());
	}

	private static ComparisonStrategy comparisonStrategyOfClass(Class<? extends ComparisonStrategy> strategyClass) {
		try {
			return strategyClass.getConstructor().newInstance();
		} catch(Exception e) {
			throw new PalindromeException("Can not instantiate an instance of class " + strategyClass.getName());
		}
	}

}
