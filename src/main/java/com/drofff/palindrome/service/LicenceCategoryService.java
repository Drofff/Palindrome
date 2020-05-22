package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.LicenceCategory;

public interface LicenceCategoryService extends SimpleEntityManager<LicenceCategory> {

	List<LicenceCategory> getAll();

	LicenceCategory getById(String id);

}
