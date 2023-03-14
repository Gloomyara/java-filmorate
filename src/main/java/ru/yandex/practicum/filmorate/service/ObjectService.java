package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.repository.ObjectsRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Set;
@Slf4j
public abstract class ObjectService<T>{
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    protected Validator validator = factory.getValidator();
    protected Set<ConstraintViolation<T>> violations;
    protected final ObjectsRepository<T> repository;
    public ObjectService(ObjectsRepository<T> repository) {
        this.repository = repository;
    }
    public Collection<T> findAll() {
        log.debug(
                "Запрос списка {} успешно выполнен, всего {}: {}",
                repository.findAll().getClass().toString(),
                repository.findAll().getClass().toString(),
                repository.get().size()
        );
        return repository.findAll();
    }
    public abstract T create(T t);
    public abstract T put(T t);
}
