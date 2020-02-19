package com.drofff.palindrome.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PageableUtils {

	private PageableUtils() {}

	public static <T> List<T> getCollectionPageOfSize(Collection<T> collection, int page, int pageSize) {
		int offset = page * pageSize;
		return collection.stream()
				.skip(offset)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	public static int countPagesOfCollection(Collection<?> collection, int pageSize) {
		int pagesCount = collection.size() / pageSize;
		if(hasAdditionalPage(collection, pageSize)) {
			pagesCount++;
		}
		return pagesCount;
	}

	private static boolean hasAdditionalPage(Collection<?> collection, int pageSize) {
		return !pagesCoverAllElementsOfCollection(collection, pageSize);
	}

	private static boolean pagesCoverAllElementsOfCollection(Collection<?> collection, int pageSize) {
		return collection.size() % pageSize == 0;
	}

}
