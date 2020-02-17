package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Brand;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
}
