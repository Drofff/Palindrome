package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Brand;

import java.util.List;

public interface BrandService {

	List<Brand> getAll();

	Brand getById(String id);

}
