package com.drofff.palindrome.grep.strategy;

import static com.drofff.palindrome.utils.StringUtils.isNotString;

import com.drofff.palindrome.exception.PalindromeException;

public class StartsWithComparisonStrategy implements ComparisonStrategy {

	@Override
	public boolean compareSourceWithFilter(Object sourceValue, Object filterValue) {
		validateIsString(sourceValue);
		validateIsString(filterValue);
		String sourceStr = (String) sourceValue;
		String filterStr = (String) filterValue;
		return sourceStr.startsWith(filterStr);
	}

	private <T> void validateIsString(T object) {
		if(isNotString(object)) {
			throw new PalindromeException("Illegal argument type " + object.getClass().getName() + ". String expected");
		}
	}

}
