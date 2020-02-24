package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.ViolationType;

@Repository
public interface ViolationTypeRepository extends MongoRepository<ViolationType, String> {
}
