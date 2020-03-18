package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.StringUtils.isNotBlank;
import static com.drofff.palindrome.utils.StringUtils.isNotString;
import static java.util.Objects.nonNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import com.drofff.palindrome.exception.PalindromeException;

public class ReflectionUtils {

	private ReflectionUtils() {}

	public static Type[] getGenericTypeParameters(Type type) {
		ParameterizedType parameterizedType = (ParameterizedType) type;
		return parameterizedType.getActualTypeArguments();
	}

	public static Optional<Field> getFieldFromClassByName(String name, Class<?> clazz) {
		try {
			Field field = clazz.getDeclaredField(name);
			return Optional.of(field);
		} catch(NoSuchFieldException e) {
			return Optional.empty();
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

	public static Class<?> classByName(String name) {
		try {
			return Class.forName(name);
		} catch(ClassNotFoundException e) {
			throw new PalindromeException("Can not find a class with name " + name);
		}
	}

	public static <T> boolean hasNonNullFieldValue(T object, Field field) {
		Object fieldValue = getFieldValueFromObject(field, object);
		return nonNull(fieldValue) && isNotBlankIfStr(fieldValue);
	}

	public static Object getFieldValueFromObject(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch(IllegalAccessException e) {
			throw new PalindromeException("Error while getting value of field " + field.getName());
		}
	}

	private static boolean isNotBlankIfStr(Object object) {
		return isNotString(object) || isNotBlank((String) object);
	}

}
