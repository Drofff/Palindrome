package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.ChangeRequest;

@Repository
public interface ChangeRequestRepository extends MongoRepository<ChangeRequest, String> {

	@Query(value = "{ approved : false }", count = true)
	int countNotApproved();

}
