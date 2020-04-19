package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.AuthenticationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuthenticationTokenRepository extends MongoRepository<AuthenticationToken, String>, TokenRepository<AuthenticationToken> {

    void deleteByExpiresAtBefore(LocalDateTime threshold);

}