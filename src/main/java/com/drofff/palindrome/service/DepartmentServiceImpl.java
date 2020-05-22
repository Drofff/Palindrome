package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends AbstractSimpleEntityManager<Department, DepartmentRepository> implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		super(departmentRepository);
		this.departmentRepository = departmentRepository;
	}

	@Override
	public boolean existsDepartmentWithId(String id) {
		return departmentRepository.findById(id).isPresent();
	}

}
