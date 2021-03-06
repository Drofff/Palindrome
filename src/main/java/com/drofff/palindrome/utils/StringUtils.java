package com.drofff.palindrome.utils;

import java.util.Objects;
import java.util.UUID;

public class StringUtils {

	private StringUtils() {}

	public static boolean areNotEqual(String str0, String str1) {
		return !str0.equals(str1);
	}

	public static String removePartFromStr(String part, String str) {
		return str.replace(part, "");
	}

	public static String removeAllDigits(String str) {
		return str.replaceAll("\\d", "");
	}

	public static String removeAllNonDigits(String str) {
		return str.replaceAll("\\D", "");
	}

	public static <T> boolean isNotString(T object) {
		return !isString(object);
	}

	private static <T> boolean isString(T object) {
		return String.class.isAssignableFrom(object.getClass());
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(String str) {
		return Objects.isNull(str) || str.trim().isEmpty();
	}

	public static String joinWithSpace(Object o0, Object o1) {
		return o0 + " " + o1;
	}

	public static String randomString() {
		return UUID.randomUUID().toString();
	}

}
