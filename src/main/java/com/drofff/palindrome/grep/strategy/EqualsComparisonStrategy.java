package com.drofff.palindrome.grep.strategy;

public class EqualsComparisonStrategy implements ComparisonStrategy {

	@Override
	public boolean compareSourceWithFilter(Object sourceValue, Object filterValue) {
		return filterValue.equals(sourceValue);
	}

}
