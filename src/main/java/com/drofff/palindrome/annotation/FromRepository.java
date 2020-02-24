package com.drofff.palindrome.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.mongodb.repository.MongoRepository;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromRepository {

	Class<? extends MongoRepository<?, String>> value();

}
