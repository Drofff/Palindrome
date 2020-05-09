package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

@Service
public class BrandServiceImpl implements BrandService, CarPropertyService {

	private final BrandRepository brandRepository;

	@Autowired
	public BrandServiceImpl(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	@Override
	public List<Brand> getAll() {
		return brandRepository.findAll();
	}

	@Override
	public void validateCarProperty(Car car) {
		String brandId = car.getBrandId();
		if(notExistsBrandWithId(brandId)) {
			throw new ValidationException("Brand with such id doesn't exist");
		}
	}

	private boolean notExistsBrandWithId(String id) {
		return !existsBrandWithId(id);
	}

	private boolean existsBrandWithId(String id) {
		return brandRepository.findById(id).isPresent();
	}

	@Override
	public Brand getById(String id) {
		validateNotNull(id, "Brand id should not be null");
		return brandRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Brand with such id doesn't exist"));
	}

}
