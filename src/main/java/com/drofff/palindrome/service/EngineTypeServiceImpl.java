package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.EngineType;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.EngineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngineTypeServiceImpl extends AbstractSimpleEntityManager<EngineType, EngineTypeRepository> implements EngineTypeService, CarPropertyService {

	private final EngineTypeRepository engineTypeRepository;

	@Autowired
	public EngineTypeServiceImpl(EngineTypeRepository engineTypeRepository) {
		super(engineTypeRepository);
		this.engineTypeRepository = engineTypeRepository;
	}

	@Override
	public void validateCarProperty(Car car) {
		String engineTypeId = car.getEngineTypeId();
		if(notExistsEngineTypeWithId(engineTypeId)) {
			throw new ValidationException("Engine type with such id doesn't exist");
		}
	}

	private boolean notExistsEngineTypeWithId(String id) {
		return !existsEngineTypeWithId(id);
	}

	private boolean existsEngineTypeWithId(String id) {
		return engineTypeRepository.findById(id).isPresent();
	}

}
