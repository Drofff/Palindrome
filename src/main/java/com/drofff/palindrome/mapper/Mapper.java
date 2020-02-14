package com.drofff.palindrome.mapper;

import java.lang.reflect.ParameterizedType;
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
		entityType = getGenericTypeParameterAtPosition(0);
		dtoType = getGenericTypeParameterAtPosition(1);
	}

	private Type getGenericTypeParameterAtPosition(int position) {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] typeArgs = parameterizedType.getActualTypeArguments();
		return typeArgs[position];
	}

	public D toDto(E entity) {
		return modelMapper.map(entity, dtoType);
	}

	public E toEntity(D dto) {
		return modelMapper.map(dto, entityType);
	}

}
