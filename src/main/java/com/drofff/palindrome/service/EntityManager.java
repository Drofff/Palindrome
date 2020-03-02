package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Entity;

public interface EntityManager {

	void update(Entity entity);

	boolean isManagingEntityOfClass(Class<?> clazz);

}
