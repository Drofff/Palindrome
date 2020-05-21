package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String>, SimpleEntityRepository<Brand> {
}
