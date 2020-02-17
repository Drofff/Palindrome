package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.BodyTypeRepository;

@Service
public class BodyTypeServiceImpl implements BodyTypeService, CarPropertyService {

	private final BodyTypeRepository bodyTypeRepository;

	@Autowired
	public BodyTypeServiceImpl(BodyTypeRepository bodyTypeRepository) {
		this.bodyTypeRepository = bodyTypeRepository;
	}

	@Override
	public List<BodyType> getAll() {
		return bodyTypeRepository.findAll();
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
