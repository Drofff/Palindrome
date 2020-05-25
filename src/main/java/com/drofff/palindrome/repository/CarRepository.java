package com.drofff.palindrome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Car;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

	Optional<Car> findByNumber(String number);

	boolean existsByBodyTypeId(String id);

	boolean existsByBrandId(String id);

	boolean existsByLicenceCategoryId(String id);

	boolean existsByEngineTypeId(String id);

}
