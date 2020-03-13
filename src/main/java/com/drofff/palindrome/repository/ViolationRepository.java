package com.drofff.palindrome.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Violation;

@Repository
public interface ViolationRepository extends MongoRepository<Violation, String> {

	List<Violation> findByViolatorId(String id);

	Page<Violation> findByViolatorId(String id, Pageable pageable);

	List<Violation> findByCarId(String id);

}
