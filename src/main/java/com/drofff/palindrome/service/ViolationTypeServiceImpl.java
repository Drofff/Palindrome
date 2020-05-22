package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Fee;
import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.repository.ViolationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

@Service
public class ViolationTypeServiceImpl extends AbstractSimpleEntityManager<ViolationType, ViolationTypeRepository> implements ViolationTypeService {

	private final ViolationTypeRepository violationTypeRepository;

	@Autowired
	public ViolationTypeServiceImpl(ViolationTypeRepository violationTypeRepository) {
		super(violationTypeRepository);
		this.violationTypeRepository = violationTypeRepository;
	}

	@Override
	public boolean existsViolationTypeWithId(String id) {
		validateNotNull(id, "Violation type id is required");
		return violationTypeRepository.findById(id).isPresent();
	}

	@Override
	protected void beforeSave(ViolationType violationType) {
		validateFee(violationType);
	}

	@Override
	protected void beforeUpdate(ViolationType violationType) {
		validateFee(violationType);
	}

	private void validateFee(ViolationType violationType) {
		Fee fee = violationType.getFee();
		validate(fee);
	}

}
