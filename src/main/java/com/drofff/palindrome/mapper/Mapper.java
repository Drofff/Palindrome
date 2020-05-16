package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Entity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.lang.reflect.Type;

import static com.drofff.palindrome.utils.ReflectionUtils.getGenericTypeParameters;

abstract class Mapper<E extends Entity, D> {

	private final ModelMapper modelMapper;
	private final TypeMap<D, E> toEntityTypeMap;

	private Class<E> entityType;
	private Class<D> dtoType;

	protected Mapper(ModelMapper modelMapper) {
		initTypes();
		this.modelMapper = modelMapper;
		this.toEntityTypeMap = initToEntityTypeMap();
	}

	@SuppressWarnings("unchecked")
	private void initTypes() {
		Type superClass = getClass().getGenericSuperclass();
		Type[] genericTypeParams = getGenericTypeParameters(superClass);
		entityType = (Class<E>) genericTypeParams[0];
		dtoType = (Class<D>) genericTypeParams[1];
	}

	private TypeMap<D, E> initToEntityTypeMap() {
		return modelMapper.createTypeMap(dtoType, entityType)
				.addMappings(mapping -> mapping.skip(E::setId));
	}

	public D toDto(E entity) {
		return modelMapper.map(entity, dtoType);
	}

	public E toEntity(D dto) {
		return toEntityTypeMap.map(dto);
	}

}