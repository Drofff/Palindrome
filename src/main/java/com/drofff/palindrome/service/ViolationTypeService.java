package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.ViolationType;

public interface ViolationTypeService extends SimpleEntityManager<ViolationType> {

	ViolationType getViolationTypeById(String id);

	boolean existsViolationTypeWithId(String id);

	List<ViolationType> getAllViolationTypes();

}
