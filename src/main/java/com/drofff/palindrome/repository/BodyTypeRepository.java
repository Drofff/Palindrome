package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.BodyType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyTypeRepository extends MongoRepository<BodyType, String>, SimpleEntityRepository<BodyType> {
}