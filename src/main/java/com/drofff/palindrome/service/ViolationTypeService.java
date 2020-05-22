package com.drofff.palindrome.service;

import com.drofff.palindrome.document.ViolationType;

public interface ViolationTypeService extends SimpleEntityManager<ViolationType> {

	boolean existsViolationTypeWithId(String id);

}