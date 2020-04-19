package com.drofff.palindrome.repository;

import java.util.Optional;

public interface TokenRepository<T> {

    Optional<T> findByValueAndUserId(String value, String userId);

}
