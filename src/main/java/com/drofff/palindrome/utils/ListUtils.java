package com.drofff.palindrome.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {

	private ListUtils() {}

	public static <E, R> List<R> applyToEachListElement(Function<E, R> function, List<E> list) {
		return list.stream()
				.map(function)
				.collect(Collectors.toList());
	}

}
