package com.drofff.palindrome.filter;

public interface ComparisonStrategy {

	 <T> boolean compare(T sourceValue, T filterValue);

}
