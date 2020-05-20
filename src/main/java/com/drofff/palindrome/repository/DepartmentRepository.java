package com.drofff.palindrome.repository;

import com.drofff.palindrome.document.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends SimpleEntityRepository<Department> {
}
