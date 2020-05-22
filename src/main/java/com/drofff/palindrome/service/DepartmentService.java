package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Department;

import java.util.List;

public interface DepartmentService extends SimpleEntityManager<Department> {

	List<Department> getAll();

	boolean existsDepartmentWithId(String id);

	Department getById(String id);

}
