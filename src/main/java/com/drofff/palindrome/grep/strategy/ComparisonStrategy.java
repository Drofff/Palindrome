package com.drofff.palindrome.grep.strategy;

public interface ComparisonStrategy {

	 <T> boolean compare(T sourceValue, T filterValue);

}
