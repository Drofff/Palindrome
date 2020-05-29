package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.Violation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViolationRepository extends MongoRepository<Violation, String> {

	List<Violation> findByViolatorId(String id);

	Page<Violation> findByViolatorId(String id, Pageable pageable);

	Page<Violation> findByOfficerIdOrderByDateTimeDesc(String officerId, Pageable pageable);

	List<Violation> findByCarId(String id);

	List<Violation> findByDateTimeAfterAndOfficerId(LocalDateTime threshold, String officerId);

	long countByViolatorIdAndPaid(String violatorId, boolean paid);

	boolean existsByViolationTypeId(String id);

}