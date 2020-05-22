package com.drofff.palindrome.service;

import com.drofff.palindrome.document.EngineType;

import java.util.List;

public interface EngineTypeService extends SimpleEntityManager<EngineType> {

	List<EngineType> getAll();

	EngineType getById(String id);

}
