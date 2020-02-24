package com.drofff.palindrome.utils;

public class StringUtils {

	private StringUtils() {}

	public static boolean areNotEqual(String str0, String str1) {
		return !str0.equals(str1);
	}

	public static String removeStrPart(String str, String part) {
		return str.replace(part, "");
	}

	public static String removeAllDigits(String str) {
		return str.replaceAll("\\d", "");
	}

	public static String removeAllNonDigits(String str) {
		return str.replaceAll("\\D", "");
	}

}
