package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Set;

@Slf4j
public abstract class ObjectService<T> {
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    protected Validator validator = factory.getValidator();
    protected Set<ConstraintViolation<T>> violations;
    protected Integer id = 1;

    public ObjectService() {
    }

    public abstract Collection<T> findAll();

    public abstract T create(T t);

    public abstract T put(T t);
}
