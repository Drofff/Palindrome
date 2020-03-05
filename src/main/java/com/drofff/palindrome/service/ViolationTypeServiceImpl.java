package com.drofff.palindrome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ViolationTypeRepository;

@Service
public class ViolationTypeServiceImpl implements ViolationTypeService {

	private final ViolationTypeRepository violationTypeRepository;

	@Autowired
	public ViolationTypeServiceImpl(ViolationTypeRepository violationTypeRepository) {
		this.violationTypeRepository = violationTypeRepository;
	}

	@Override
	public ViolationType getViolationTypeById(String id) {
		return violationTypeRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Violation Type with such id doesn't exist"));
	}

}
