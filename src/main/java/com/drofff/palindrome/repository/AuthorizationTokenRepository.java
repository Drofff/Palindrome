package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.AuthorizationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorizationTokenRepository extends MongoRepository<AuthorizationToken, String> {

    Optional<AuthorizationToken> findByToken(String token);

    List<AuthorizationToken> findByDueDateTimeLessThan(LocalDateTime dateTime);

}
