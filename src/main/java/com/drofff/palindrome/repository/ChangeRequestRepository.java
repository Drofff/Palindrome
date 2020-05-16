package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.ChangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChangeRequestRepository extends MongoRepository<ChangeRequest, String> {

	@Query(value = "{ approved : false }", count = true)
	int countNotApproved();

	Page<ChangeRequest> findBySenderIdOrderByDateTimeDesc(String senderId, Pageable pageable);

	List<ChangeRequest> findByDateTimeAfterAndApprovedAndSenderId(LocalDateTime dateTime, boolean approved, String senderId);

}
