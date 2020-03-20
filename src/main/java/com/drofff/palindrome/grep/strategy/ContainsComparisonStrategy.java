package com.drofff.palindrome.grep.strategy;

import com.drofff.palindrome.exception.PalindromeException;

import java.util.Collection;

public class ContainsComparisonStrategy implements ComparisonStrategy {

    @Override
    public boolean compareSourceWithFilter(Object sourceValue, Object filterValue) {
        validateIsCollection(sourceValue);
        Collection<?> collection = (Collection<?>) sourceValue;
        return collection.contains(filterValue);
    }

    private void validateIsCollection(Object object) {
        if(isNotCollection(object)) {
            throw new PalindromeException("Contains strategy expects collection as a source value");
        }
    }

    private boolean isNotCollection(Object object) {
        return !isCollection(object);
    }

    private boolean isCollection(Object object) {
        return object instanceof Collection;
    }

}
