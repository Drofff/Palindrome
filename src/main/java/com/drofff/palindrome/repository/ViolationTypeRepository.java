package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.ViolationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationTypeRepository extends MongoRepository<ViolationType, String>, SimpleEntityRepository<ViolationType> {
}
