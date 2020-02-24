package com.drofff.palindrome.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.drofff.palindrome.type.CollectionPage;

public class CollectionPageUtils {

	private CollectionPageUtils() {}

	public static <T> List<T> getContentOfPage(CollectionPage<T> collectionPage) {
		int pageSize = collectionPage.getPageSize();
		int offset = collectionPage.getPageNumber() * pageSize;
		return collectionPage.getCollection().stream()
				.skip(offset)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	public static int countPages(CollectionPage<?> collectionPage) {
		Collection<?> collection = collectionPage.getCollection();
		int pageSize = collectionPage.getPageSize();
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
