package com.drofff.palindrome.utils;

import java.lang.reflect.Field;
import java.util.Optional;

import com.drofff.palindrome.exception.PalindromeException;

public class ReflectionUtils {

	private ReflectionUtils() {}

	public static Optional<Field> getFieldFromClassByName(String name, Class<?> clazz) {
		try {
			Field field = clazz.getDeclaredField(name);
			return Optional.of(field);
		} catch(NoSuchFieldException e) {
			return Optional.empty();
		}
	}

	public static Object getFieldValueFromObject(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch(IllegalAccessException e) {
			throw new PalindromeException("Error while getting value of field " + field.getName());
		}
	}

	public static void setFieldValueIntoObject(Field field, Object value, Object object) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch(IllegalAccessException e) {
			throw new PalindromeException("Error while setting value of field " + field.getName());
		}
	}

}
