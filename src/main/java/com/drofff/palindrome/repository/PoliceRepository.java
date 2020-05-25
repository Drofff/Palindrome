package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.Police;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoliceRepository extends MongoRepository<Police, String> {

	Optional<Police> findByUserId(String id);

	boolean existsByDepartmentId(String id);

}