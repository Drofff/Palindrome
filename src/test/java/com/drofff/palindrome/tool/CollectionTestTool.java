package com.drofff.palindrome.tool;

import com.drofff.palindrome.exception.PalindromeException;

import java.util.Collection;

public class CollectionTestTool {

    private CollectionTestTool() {}

    public static <T> T getFirstElement(Collection<T> collection) {
        return collection.stream()
                .findFirst()
                .orElseThrow(() -> new PalindromeException("Collections is empty"));
    }

}
