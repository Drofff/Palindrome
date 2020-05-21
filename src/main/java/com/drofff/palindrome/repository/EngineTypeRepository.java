package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.EngineType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineTypeRepository extends MongoRepository<EngineType, String>, SimpleEntityRepository<EngineType> {
}
