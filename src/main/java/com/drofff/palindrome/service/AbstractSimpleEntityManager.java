package com.drofff.palindrome.service;

import com.drofff.palindrome.document.SimpleEntity;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.SimpleEntityRepository;

import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.ValidationUtils.*;

public abstract class AbstractSimpleEntityManager<T extends SimpleEntity> implements SimpleEntityManager<T> {

    private static final String NO_ENTITY_WITH_ID_MESSAGE = "Entity with such id doesn't exist";

    private final SimpleEntityRepository<T> simpleEntityRepository;

    protected AbstractSimpleEntityManager(SimpleEntityRepository<T> simpleEntityRepository) {
        this.simpleEntityRepository = simpleEntityRepository;
    }

    @Override
    public void create(T t) {
        validate(t);
        t.setId(null);
        validateHasUniqueName(t);
        beforeSave(t);
        simpleEntityRepository.save(t);
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
        simpleEntityRepository.save(t);
    }

    private boolean changedName(T t) {
        return simpleEntityRepository.findById(t.getId())
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
        return simpleEntityRepository.findByName(name).isPresent();
    }

    protected void beforeUpdate(T t) {
        // Hook for children classes
    }

    @Override
    public void delete(T t) {
        validateNotNull(t);
        validateEntityHasId(t);
        validateExists(t);
        simpleEntityRepository.deleteById(t.getId());
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
        return simpleEntityRepository.findById(id).isPresent();
    }

}
