package com.drofff.palindrome.service;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.BodyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

@Service
public class BodyTypeServiceImpl extends AbstractSimpleEntityManager<BodyType> implements BodyTypeService, CarPropertyService {

	private final BodyTypeRepository bodyTypeRepository;

	@Autowired
	public BodyTypeServiceImpl(BodyTypeRepository bodyTypeRepository) {
		super(bodyTypeRepository);
		this.bodyTypeRepository = bodyTypeRepository;
	}

	@Override
	public List<BodyType> getAll() {
		return bodyTypeRepository.findAll();
	}

	@Override
	public BodyType getById(String id) {
		validateNotNull(id, "Id should be provided");
		return bodyTypeRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Body type with such an id doesn't exist"));
	}

	@Override
	public void validateCarProperty(Car car) {
		String bodyTypeId = car.getBodyTypeId();
		if(notExistsBodyTypeWithId(bodyTypeId)) {
			throw new ValidationException("Body type with such id doesn't exist");
		}
	}

	private boolean notExistsBodyTypeWithId(String id) {
		return !existsBodyTypeWithId(id);
	}

	private boolean existsBodyTypeWithId(String id) {
		return bodyTypeRepository.findById(id).isPresent();
	}

}
