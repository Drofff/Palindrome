package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.BodyType;

public interface BodyTypeService extends SimpleEntityManager<BodyType> {

	List<BodyType> getAll();

	BodyType getById(String id);

}
