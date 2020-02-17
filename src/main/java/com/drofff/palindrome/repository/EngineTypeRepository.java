package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.EngineType;

@Repository
public interface EngineTypeRepository extends MongoRepository<EngineType, String> {
}
