package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.Department;

public interface DepartmentService {

	List<Department> getAll();

	boolean existsDepartmentWithId(String id);

}
