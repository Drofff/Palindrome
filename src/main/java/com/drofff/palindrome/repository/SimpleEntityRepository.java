package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SimpleEntityRepository<T> extends MongoRepository<T, String> {

    Optional<T> findByName(String name);

}
