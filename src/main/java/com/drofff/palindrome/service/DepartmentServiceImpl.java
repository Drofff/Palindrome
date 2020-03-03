package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	public List<Department> getAll() {
		return departmentRepository.findAll();
	}

	@Override
	public boolean existsDepartmentWithId(String id) {
		return departmentRepository.findById(id).isPresent();
	}

}
