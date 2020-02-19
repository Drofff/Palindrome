package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Car;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

}
