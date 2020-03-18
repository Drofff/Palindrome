package com.drofff.palindrome.grep.strategy;

public class EqualsComparisonStrategy implements ComparisonStrategy {

	@Override
	public <T> boolean compare(T sourceValue, T filterValue) {
		return filterValue.equals(sourceValue);
	}

}
