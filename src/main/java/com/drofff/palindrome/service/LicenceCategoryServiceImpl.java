package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.LicenceCategory;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.LicenceCategoryRepository;

@Service
public class LicenceCategoryServiceImpl implements LicenceCategoryService, CarPropertyService {

	private final LicenceCategoryRepository licenceCategoryRepository;

	@Autowired
	public LicenceCategoryServiceImpl(LicenceCategoryRepository licenceCategoryRepository) {
		this.licenceCategoryRepository = licenceCategoryRepository;
	}

	@Override
	public List<LicenceCategory> getAll() {
		return licenceCategoryRepository.findAll();
	}

	@Override
	public void validateCarProperty(Car car) {
		String licenceCategoryId = car.getLicenceCategoryId();
		if(notExistsLicenceCategoryWithId(licenceCategoryId)) {
			throw new ValidationException("Licence category with such id doesn't exist");
		}
	}

	private boolean notExistsLicenceCategoryWithId(String id) {
		return !existsLicenceCategoryWithId(id);
	}

	private boolean existsLicenceCategoryWithId(String id) {
		return licenceCategoryRepository.findById(id).isPresent();
	}

}
