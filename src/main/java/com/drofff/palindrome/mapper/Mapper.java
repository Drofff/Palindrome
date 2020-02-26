package com.drofff.palindrome.mapper;

import static com.drofff.palindrome.utils.ReflectionUtils.getGenericTypeParameters;

import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;

abstract class Mapper<E, D> {

	private final ModelMapper modelMapper;

	private Type entityType;
	private Type dtoType;

	protected Mapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		initTypes();
	}

	private void initTypes() {
		Type superClass = getClass().getGenericSuperclass();
		Type[] genericTypeParams = getGenericTypeParameters(superClass);
		entityType = genericTypeParams[0];
		dtoType = genericTypeParams[1];
	}

	public D toDto(E entity) {
		return modelMapper.map(entity, dtoType);
	}

	public E toEntity(D dto) {
		return modelMapper.map(dto, entityType);
	}

}
