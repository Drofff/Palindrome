package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.LicenceCategory;

@Repository
public interface LicenceCategoryRepository extends MongoRepository<LicenceCategory, String> {
}
