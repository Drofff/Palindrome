package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ReflectionUtils.getFieldFromClassByName;
import static com.drofff.palindrome.utils.ReflectionUtils.getFieldValueFromObject;
import static com.drofff.palindrome.utils.ReflectionUtils.setFieldValueIntoObject;
import static com.drofff.palindrome.utils.StringUtils.removeStrPart;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.exception.PalindromeException;

@Service
public class MappingsResolverImpl implements MappingsResolver {

	private static final String MAPPING_SUFFIX = "Id";

	private final List<MongoRepository<?, String>> repositories;

	@Autowired
	public MappingsResolverImpl(List<MongoRepository<?, String>> repositories) {
		this.repositories = repositories;
	}

	@Override
	public <F, T> T resolveMappings(F source, T destination) {
		validateMappings(source, destination);
		Arrays.stream(source.getClass().getDeclaredFields())
				.filter(this::hasMappingAnnotation)
				.forEach(field -> resolveFieldMapping(field, source, destination));
		return destination;
	}

	private void validateMappings(Object source, Object destination) {
		validateNotNull(source, "Mapping source should not be null");
		validateNotNull(destination, "Mapping destination should not be null");
	}

	private boolean hasMappingAnnotation(Field field) {
		Annotation mappingAnnotation = field.getAnnotation(FromRepository.class);
		return Objects.nonNull(mappingAnnotation);
	}

	private <F, T> void resolveFieldMapping(Field mappedField, F source, T destination) {
		Optional<Field> destinationFieldOptional = getDestinationFieldOfMapping(mappedField, destination);
		if(destinationFieldOptional.isPresent()) {
			Optional<?> mappedValueOptional = getMappedValueIfPresent(mappedField, source);
			mappedValueOptional.ifPresent(val -> setFieldValueIntoObject(destinationFieldOptional.get(), val, destination));
		}
	}

	private <T> Optional<Field> getDestinationFieldOfMapping(Field mappedField, T destination) {
		String destinationFieldName = removeMappingSuffix(mappedField.getName());
		return getFieldFromClassByName(destinationFieldName, destination.getClass());
	}

	private String removeMappingSuffix(String name) {
		return removeStrPart(name, MAPPING_SUFFIX);
	}

	private <F> Optional<?> getMappedValueIfPresent(Field mappingIdField, F source) {
		String mappingId = (String) getFieldValueFromObject(mappingIdField, source);
		FromRepository mappingAnnotation = getMappingAnnotation(mappingIdField);
		MongoRepository<?, String> mappedRepository = getRepositoryMappedByAnnotation(mappingAnnotation);
		return mappedRepository.findById(mappingId);
	}

	private FromRepository getMappingAnnotation(Field field) {
		return field.getAnnotation(FromRepository.class);
	}

	private MongoRepository<?, String> getRepositoryMappedByAnnotation(FromRepository mappingAnnotation) {
		Class<?> repositoryClass = mappingAnnotation.value();
		return repositories.stream()
				.filter(repo -> isInstanceOfClass(repo, repositoryClass))
				.findFirst()
				.orElseThrow(() -> new PalindromeException("Can not resolve repository of class " + repositoryClass.getName()));
	}

	private boolean isInstanceOfClass(Object object, Class<?> clazz) {
		Class<?> objectClass = object.getClass();
		return clazz.isAssignableFrom(objectClass);
	}

}
