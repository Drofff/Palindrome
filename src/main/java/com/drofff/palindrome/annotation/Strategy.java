package com.drofff.palindrome.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.drofff.palindrome.grep.strategy.ComparisonStrategy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {

	Class<? extends ComparisonStrategy> value();

}
