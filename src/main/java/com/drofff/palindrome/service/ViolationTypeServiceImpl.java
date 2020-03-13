package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.List;

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
		validateNotNull(id, "Violation id is required");
		return violationTypeRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Violation type with such id doesn't exist"));
	}

	@Override
	public boolean existsViolationTypeWithId(String id) {
		validateNotNull(id, "Violation type id is required");
		return violationTypeRepository.findById(id).isPresent();
	}

	@Override
	public List<ViolationType> getAllViolationTypes() {
		return violationTypeRepository.findAll();
	}

}
