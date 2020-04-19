package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.ActivationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationTokenRepository extends MongoRepository<ActivationToken, String>, TokenRepository<ActivationToken> {

    void deleteByValue(String value);

}
