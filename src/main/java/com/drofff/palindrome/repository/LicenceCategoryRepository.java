package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.LicenceCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceCategoryRepository extends MongoRepository<LicenceCategory, String>, SimpleEntityRepository<LicenceCategory> {
}
