package com.drofff.palindrome.filter;

public class EqualsComparisonStrategy implements ComparisonStrategy {

	@Override
	public <T> boolean compare(T sourceValue, T filterValue) {
		return filterValue.equals(sourceValue);
	}

}
