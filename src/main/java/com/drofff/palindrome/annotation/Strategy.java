package com.drofff.palindrome.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.drofff.palindrome.filter.ComparisonStrategy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {

	Class<? extends ComparisonStrategy> value();

}
