package com.drofff.palindrome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.UserBlock;

@Repository
public interface UserBlockRepository extends MongoRepository<UserBlock, String> {

	Optional<UserBlock> findByUserId(String id);

}
