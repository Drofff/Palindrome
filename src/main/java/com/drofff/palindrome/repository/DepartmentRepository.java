package com.drofff.palindrome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drofff.palindrome.document.Department;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
}
