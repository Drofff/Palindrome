package com.drofff.palindrome.repository;

import java.util.Optional;

public interface SimpleEntityRepository<T> {

    Optional<T> findByName(String name);

}
