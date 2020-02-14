package com.drofff.palindrome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Driver;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {

	Optional<Driver> findByUserId(String userId);

}
