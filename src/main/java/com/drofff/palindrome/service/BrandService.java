package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.Brand;

public interface BrandService {

	List<Brand> getAll();

	Brand getById(String id);

}
