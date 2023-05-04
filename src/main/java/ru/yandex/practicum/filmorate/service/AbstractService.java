package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class AbstractService<T extends Entity, R extends Repository<T>> implements Service<T> {

    protected final R storage;

    protected AbstractService(R storage) {
        this.storage = storage;
    }

    public T findById(Long id) {
        var optionalT = storage.findById(id);
        if (optionalT.isPresent()) {
            log.info("Найден: {}.", optionalT.get());
            return optionalT.get();
        }
        throw new EntityNotFoundException(
                String.format("id=%d не найден!", id));
    }

    public List<T> findAll() {
        var t = storage.findAll();
        log.info("Найден список: {}.", Arrays.toString(t.toArray()));
        return t;
    }

    public T create(T t) {
        storage.save(t);
        log.info("Создан: {}.", t);
        return findById(t.getId());
    }

    public T update(T t) {
        findById(t.getId());
        storage.update(t);
        log.info("Обновлен: {}.", t);
        return findById(t.getId());
    }

    public void delete(Long id) {
        T t = findById(id);
        storage.delete(id);
        log.info("Удален: {}.", t);
    }
}
