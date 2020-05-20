package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends AbstractSimpleEntityManager<Department> implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		super(departmentRepository);
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

	@Override
	public Department getDepartmentById(String id) {
		return departmentRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Department with such id doesn't exist"));
	}

}
