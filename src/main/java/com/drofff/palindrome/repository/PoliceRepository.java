package com.drofff.palindrome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Police;

@Repository
public interface PoliceRepository extends MongoRepository<Police, String> {

	Optional<Police> findByUserId(String id);

}