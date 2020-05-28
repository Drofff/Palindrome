package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAppRepository extends MongoRepository<UserApp, String> {

    Optional<UserApp> findByUserId(String userId);

}