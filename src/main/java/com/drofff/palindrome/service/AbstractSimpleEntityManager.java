package com.drofff.palindrome.service;

import com.drofff.palindrome.document.SimpleEntity;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.SimpleEntityRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Type;
import java.util.List;

import static com.drofff.palindrome.utils.ReflectionUtils.getGenericTypeParameters;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.ValidationUtils.*;

public abstract class AbstractSimpleEntityManager<T extends SimpleEntity,
        R extends SimpleEntityRepository<T> & MongoRepository<T, String>> implements SimpleEntityManager<T> {

    private static final String NO_ENTITY_WITH_ID_MESSAGE = "Entity with such id doesn't exist";

    private final R repository;

    protected AbstractSimpleEntityManager(R repository) {
        this.repository = repository;
    }

    @Override
    public void create(T t) {
        validate(t);
        t.setId(null);
        validateHasUniqueName(t);
        beforeSave(t);
        repository.save(t);
    }

    protected void beforeSave(T t) {
        // Hook for children classes
    }

    @Override
    public void update(T t) {
        validate(t);
        validateEntityHasId(t);
        validateExists(t);
        if(changedName(t)) {
            validateHasUniqueName(t);
        }
        beforeUpdate(t);
        repository.save(t);
    }

    private boolean changedName(T t) {
        return repository.findById(t.getId())
                .map(originalT -> {
                    String originalName = originalT.getName();
                    return areNotEqual(originalName, t.getName());
                }).orElseThrow(() -> new ValidationException(NO_ENTITY_WITH_ID_MESSAGE));
    }

    private void validateHasUniqueName(T t) {
        if(existsSimpleEntityWithName(t.getName())) {
            throw new ValidationException("The name is already in use");
        }
    }

    private boolean existsSimpleEntityWithName(String name) {
        return repository.findByName(name).isPresent();
    }

    protected void beforeUpdate(T t) {
        // Hook for children classes
    }

    @Override
    public void delete(T t) {
        validateNotNull(t);
        validateEntityHasId(t);
        validateExists(t);
        repository.deleteById(t.getId());
    }

    private void validateExists(T t) {
        if(missingSimpleEntityWithId(t.getId())) {
            throw new ValidationException(NO_ENTITY_WITH_ID_MESSAGE);
        }
    }

    private boolean missingSimpleEntityWithId(String id) {
        return !existsSimpleEntityWithId(id);
    }

    private boolean existsSimpleEntityWithId(String id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T getById(String id) {
        validateNotNull(id, "Id should be provided");
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException(noEntityWithIdMessage()));
    }

    private String noEntityWithIdMessage() {
        String entityName = getEntityClass().getSimpleName();
        return entityName + " with such id doesn't exist";
    }

    private Class<?> getEntityClass() {
        Type[] typeParams = getGenericTypeParameters(getClass());
        return (Class<?>) typeParams[0];
    }

}
