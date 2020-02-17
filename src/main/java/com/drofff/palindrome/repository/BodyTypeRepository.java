package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.BodyType;

@Repository
public interface BodyTypeRepository extends MongoRepository<BodyType, String> {

}
